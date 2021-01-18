package com.katyrin.weatherapp.observer;

public interface Observer {
    void updateCityName(String cityName);
    void updateShowDetails(boolean isWindy, boolean isHumidity, boolean isPressure);
}
