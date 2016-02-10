package com.example.yhop.myweather.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhop.myweather.LocationHelper;
import com.example.yhop.myweather.R;
import com.example.yhop.myweather.Service.ServiceHelper;
import com.example.yhop.myweather.WeatherApplication;
import com.example.yhop.myweather.weather.Current;
import com.example.yhop.myweather.weather.Day;
import com.example.yhop.myweather.weather.Forecast;
import com.example.yhop.myweather.weather.Hour;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.locationLabel)
    TextView mLocationLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;
    // private Current mCurrent;
    private Forecast mForecast;
    private LocationHelper locationHelper;
    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getLocation();

        mLocationLabel.setText(LocationHelper.getCity());
        mProgressBar.setVisibility(View.INVISIBLE);

//        Log.i("API_KEY", WeatherApplication.getInstance().getApiKey());

        ServiceHelper.getData(WeatherApplication.getInstance().getApiKey(), latitude, longitude);

        getForecast(latitude, longitude);

        Log.d(TAG, "Main UI code is running!");

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                getForecast(latitude, longitude);
            }
        });

    } // end of onCreate

    private void getLocation() {
        locationHelper = new LocationHelper(MainActivity.this);
        latitude = LocationHelper.getCurrentLatitude();
        longitude = LocationHelper.getCurrentLongitude();
    }


    private void getForecast(double latitude, double longitude) {
        String apiUrl = WeatherApplication.getInstance().getApiUrl();
        String apiKey = WeatherApplication.getInstance().getApiKey();
        String forecastURL = apiUrl + apiKey + "/" + latitude + "," + longitude;

        if(isNetworkAvailable()){
            toggleRefresh();
        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url(forecastURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });
                alertUserAboutError();
                // AlertDialogFragment dialog = new AlertDialogFragment();
                // dialog.show(getFragmentManager(), "error_dialog");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleRefresh();
                    }
                });

                try {
                    String jsonData = response.body().string();
                    // Response response = call.execute();
                    // Log.v(TAG, response.body().string());

//                    Log.v(TAG, jsonData);

                    if (response.isSuccessful()) {
                        // mCurrent = getCurrentDetails(jsonData);
                        mForecast = parseForecastDetials(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay();
                            }
                        });
                    } else {
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    // e.printStackTrace();
                    Log.e(TAG, "Exception caught: ", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            } // end of onResponse

        }); // end of call.enqueue

        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();

        } // end of isNetworkAvailable if_else
    }

    private void toggleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE){
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        Current current = mForecast.getCurrently();
        mTemperatureLabel.setText(current.getTemperature()+"");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), current.getIconId(), null);
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetials(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrently(getCurrentDetails(jsonData));
        forecast.setHourly(getHourlyForecast(jsonData));
        forecast.setDaily(getDailyForecast(jsonData));

        return forecast;
    }

    private Day[] getDailyForecast(String jsonData)throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for(int i = 0; i < data.length(); i ++){
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();
            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            days[i] = day;
        }

        return days;
    }

    private Hour[] getHourlyForecast(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for(int i = 0; i < data.length(); i++){
            JSONObject jsonHour = data.getJSONObject(i);
            Hour hour = new Hour();
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i] = hour;
        }

        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON :" + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIconId(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimezone(timezone);

        Log.d(TAG, current.getFormattedTime());

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick (R.id.dailyButton)
    public void startDailyActivity(View view){
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDaily());
        startActivity(intent);
    }

    @OnClick (R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourly());
        startActivity(intent);
    }

}
