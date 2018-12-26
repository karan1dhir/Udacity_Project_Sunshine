package com.example.karandhir.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karandhir.sunshine.Utilities.NetworkUtils;
import com.example.karandhir.sunshine.Utilities.OpenWeatherJsonUtils;
import com.example.karandhir.sunshine.data.SunshinePreferences;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mRecyclerView = findViewById(R.id.recyclerview_forecast);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter();
        mRecyclerView.setAdapter(mForecastAdapter);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        loadWeatherData();

    }
    private void showWeatherDataView(){
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.forecast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_refresh){
            mForecastAdapter.setWeatherData(null);
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        // COMPLETED (6) Override the doInBackground method to perform your network requests
        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildURL(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (weatherData != null) {
                showWeatherDataView();
                // COMPLETED (45) Instead of iterating through every string, use mForecastAdapter.setWeatherData and pass in the weather data
                mForecastAdapter.setWeatherData(weatherData);
            } else {
                showErrorMessage();
            }
        }
    }
}
