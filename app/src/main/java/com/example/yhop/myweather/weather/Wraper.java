package com.example.yhop.myweather.weather;

import java.util.List;

public class Wraper {
    Current currently;
    List<Hour> hourly;
    List<Day> daily;

    public Current getCurrently() {
        return currently;
    }

    public void setCurrently(Current currently) {
        this.currently = currently;
    }

    public List<Hour> getHourly() {
        return this.getHourly();
    }

    public void setHourly(List<Hour> hourly) {
        this.hourly = hourly;
    }

    public List<Day> getDaily() {
        return this.getDaily();
    }

    public void setDaily(List<Day> daily) {
        this.daily = daily;
    }
}
