package com.katyrin.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.katyrin.weatherapp.model.WeatherRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Request {

    public interface RequestListener {
        void requestListenerCallBack(int i, Handler handler, WeatherRequest weatherRequest);
    }

    private final String urlString;
    private final Context context;
    private final RequestListener listener;

    public static final String TAG = "WEATHER";
    public static final String WEATHER_API_KEY = BuildConfig.WEATHER_API_KEY;

    public Request(String cityName, Context context) {
        this.context = context;
        listener = (RequestListener) context;
        urlString = context.getString(R.string.owm_url) + cityName + context.getString(R.string.measure) +
                context.getString(R.string.request_language) + WEATHER_API_KEY;
        startRequest(1);
    }

    public Request(float lat, float lon, Context context) {
        this.context = context;
        listener = (RequestListener) context;
        urlString = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat +
                "&lon=" + lon + "&exclude=current,minutely,hourly,alerts" +
                context.getString(R.string.measure) +
                context.getString(R.string.request_language) + WEATHER_API_KEY;
        startRequest(2);
    }

    private void startRequest(int i) {
        try {
            final URL url = new URL(urlString);
            final Handler handler = new Handler(Looper.getMainLooper());

            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    String result = getLines(in);
                    Gson gson = new Gson();

                    final WeatherRequest weatherRequest =
                            gson.fromJson(result, WeatherRequest.class);
                    listener.requestListenerCallBack(i, handler, weatherRequest);
                } catch (Exception e) {
                    Log.e(TAG, "Fail connection", e);
                    e.printStackTrace();
                    Snackbar.make(((Activity) context).findViewById(R.id.mainLayout), R.string.city_not_found,
                            Snackbar.LENGTH_LONG).setAnchorView(R.id.nav_view).show();
                } finally {
                    if (null != urlConnection)
                        urlConnection.disconnect();
                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(TAG, "Fail URI", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        StringBuilder rawData = new StringBuilder(1024);
        String tempVariable;

        while (true) {
            try {
                tempVariable = in.readLine();
                if (tempVariable == null) break;
                rawData.append(tempVariable).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rawData.toString();
    }
}