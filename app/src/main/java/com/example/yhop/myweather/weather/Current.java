package com.example.yhop.myweather.weather;

import com.example.yhop.myweather.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by YHoP on 10/16/15.
 */
public class Current {
    private String mIcon;
    private double mHumidity;
    private long mTime;
    private double mTemperature;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getTimeZone() { return mTimeZone; }

    public void setTimeZone(String timeZone) { mTimeZone = timeZone; }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public void setIconId(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));
        Date dateTime = new Date(getTime() * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getPrecipChance() {
        double precipPrecentage = mPrecipChance * 100;
        return (int) Math.round(precipPrecentage);
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getHumidity() { return mHumidity; }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }
}
