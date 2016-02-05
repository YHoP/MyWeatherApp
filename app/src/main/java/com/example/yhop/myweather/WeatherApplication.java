package com.example.yhop.myweather;

import android.app.Application;

public class WeatherApplication extends Application {
    private static WeatherApplication mApplication;
    private String api_url;
    private String api_key;

    public static WeatherApplication getInstance() {
        return mApplication;
    }

    public String getApiUrl() {
        return api_url;
    }

    public String getApiKey() {
        return api_key;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        api_url = "https://api.forecast.io/forecast/";
        api_key = getResources().getString(R.string.api_key);
    }


}
