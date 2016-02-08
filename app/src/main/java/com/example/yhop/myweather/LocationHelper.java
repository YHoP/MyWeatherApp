package com.example.yhop.myweather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper extends Service implements LocationListener {

    private static double latitude;
    private static double longitude;
    private static String city;
    private static String country;
    protected LocationManager locationManager;
    private Context mContext;
    private Location location;

    public LocationHelper(Context context) {
        this.mContext = context;

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        getLocation();
    }

    public static double getCurrentLatitude() {
        return latitude;
    }

    public static double getCurrentLongitude() {
        return longitude;
    }

    public static String getCity() {
        return city;
    }

    public static String getCountry() {
        return country;
    }

    private void getLocation() {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
//                city = addresses.get(0).getLocality();
                city = addresses.get(0).getAddressLine(1);
//                country = addresses.get(0).getAddressLine(2);
                Log.i("City Name: ", city);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
