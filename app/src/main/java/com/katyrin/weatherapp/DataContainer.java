package com.katyrin.weatherapp;

public class DataContainer {

    boolean isShowFeelTemperature;

    private static DataContainer instance;

    private DataContainer(){}

    static DataContainer getInstance(){
        if (instance == null)
            instance = new DataContainer();
        return instance;
    }
}
