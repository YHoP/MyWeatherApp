package com.example.yhop.myweather.weather;

import com.example.yhop.myweather.R;

public class Forecast {
    private Current currently;
    private Hour[] hourly;
    private Day[] daily;

    public static int getIconId(String iconString){

        // clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
        int iconId = R.mipmap.clear_day;

        if(iconString.equals("clear-day")){
            iconId = R.mipmap.clear_day;
        }else if(iconString.equals("clear-night")){
            iconId = R.mipmap.clear_night;
        }else if(iconString.equals("rain")){
            iconId = R.mipmap.rain;
        }else if(iconString.equals("snow")){
            iconId = R.mipmap.snow;
        }else if(iconString.equals("sleet")){
            iconId = R.mipmap.sleet;
        }else if(iconString.equals("wind")){
            iconId = R.mipmap.wind;
        }else if(iconString.equals("fog")){
            iconId = R.mipmap.fog;
        }else if(iconString.equals("cloudy")){
            iconId = R.mipmap.cloudy;
        }else if(iconString.equals("partly-cloudy-day")){
            iconId = R.mipmap.partly_cloudy;
        }else if(iconString.equals("partly-cloudy-night")){
            iconId = R.mipmap.cloudy_night;
        }

        return iconId;
    }

    public Current getCurrently() {
        return currently;
    }

    public void setCurrently(Current current) {
        this.currently = current;
    }

    public Hour[] getHourly() {
        return hourly;
    }

    public void setHourly(Hour[] hourly) {
        this.hourly = hourly;
    }

    public Day[] getDaily() {
        return daily;
    }

    public void setDaily(Day[] daily) {
        this.daily = daily;
    }
}
