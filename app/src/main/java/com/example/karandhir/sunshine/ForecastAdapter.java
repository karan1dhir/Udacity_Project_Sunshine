package com.example.karandhir.sunshine;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {
    private String[] mWeatherData;
    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        int layoutIDforListItem = R.layout.forecast_list_item;
        LayoutInflater inflator = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflator.inflate(layoutIDforListItem,viewGroup,shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder forecastAdapterViewHolder, int i) {
        String weatherForThisDay = mWeatherData[i];
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder {



        public final TextView mWeatherTextView;
        public ForecastAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
         mWeatherTextView = itemView.findViewById(R.id.tv_weather_data);
        }
    }
}
