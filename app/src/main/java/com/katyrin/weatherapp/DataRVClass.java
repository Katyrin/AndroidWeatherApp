package com.katyrin.weatherapp;

public class DataRVClass {
    String dayName;
    String dayTemperature;
    String icon;
    String pressure;
    String humidity;
    String windSpeed;

    public DataRVClass(String dayName, String dayTemperature, String icon,
                       String windSpeed, String humidity, String pressure) {
        this.dayName = dayName;
        this.dayTemperature = dayTemperature;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}
