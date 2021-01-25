package com.katyrin.weatherapp;

public class DataContainer {

    public boolean isShowFeelTemperature;
    public boolean isDarkLightCheck;
    public boolean isShowDetails;
    public boolean isShowWind;
    public boolean isShowHumidity;
    public boolean isShowPressure;
    public int daysCount;
    public String dayName;
    public String cityName;

    private static DataContainer instance;

    private DataContainer(){}

    public static DataContainer getInstance(){
        if (instance == null)
            instance = new DataContainer();
        return instance;
    }
}
