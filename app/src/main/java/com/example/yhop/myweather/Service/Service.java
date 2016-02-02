package com.example.yhop.myweather.Service;

import com.example.yhop.myweather.weather.Wraper;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface Service {
    @GET("{api_key}/{latitude},{longitude}")
    Call<Wraper> getWeather(@Path("api_key") String api_key,
                            @Path("latitude") double latitude,
                            @Path("longitude") double longitude);


}
