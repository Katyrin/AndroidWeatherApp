package com.katyrin.weatherapp;

public interface IRVDaysOnItemClick {
    void onItemClicked(String dayName, String dayTemperature, int drawable,
                       String wind, String humidity, String pressure);
}
