package com.example.yhop.myweather.Service;

import android.util.Log;

import com.example.yhop.myweather.weather.Current;
import com.example.yhop.myweather.weather.Wraper;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ServiceHelper {
    private final static Retrofit.Builder mRetrofitBuilder = new Retrofit.Builder();

    private ServiceHelper() {
    }

    public static Retrofit.Builder getBuilder() {
        return mRetrofitBuilder;
    }

    public static Retrofit getRetrofit(String url) {
        return getBuilder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Service getWeatherService(String url) {
        return getRetrofit(url).create(Service.class);
    }


    public static void getData(String api_key, double latitude, double longitude) {
        Call<Wraper> call = getWeatherService("https://api.forecast.io/forecast/").getWeather(api_key, latitude, longitude);
        call.enqueue(new Callback<Wraper>() {
            @Override
            public void onResponse(Response<Wraper> response, Retrofit retrofit) {

                Current current = response.body().getCurrently();
                Log.i("Wraper current", current.getSummary());

//                Hour hour = response.body().getHourly().get(0);
//                Log.i("Wraper hour", hour.getSummary());
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
