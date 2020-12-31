package com.katyrin.weatherapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CitySelectionActivity extends AppCompatActivity {

    private CheckBox windCheckBox;
    private CheckBox pressureCheckBox;
    private CheckBox humidityCheckBox;
    private static final String windDataKey = "windDataKey";
    private static final String pressureDataKey = "pressureDataKey";
    private static final String humidityDataKey = "humidityDataKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_selection_activity);

        windCheckBox = findViewById(R.id.windCheckBox);
        pressureCheckBox = findViewById(R.id.pressureCheckBox);
        humidityCheckBox = findViewById(R.id.humidityCheckBox);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean windBoolean = windCheckBox.isChecked();
        boolean pressureBoolean = pressureCheckBox.isChecked();
        boolean humidityBoolean = humidityCheckBox.isChecked();

        outState.putBoolean(windDataKey, windBoolean);
        outState.putBoolean(pressureDataKey, pressureBoolean);
        outState.putBoolean(humidityDataKey, humidityBoolean);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean windBoolean = savedInstanceState.getBoolean(windDataKey);
        boolean pressureBoolean = savedInstanceState.getBoolean(pressureDataKey);
        boolean humidityBoolean = savedInstanceState.getBoolean(humidityDataKey);

        windCheckBox.setChecked(windBoolean);
        pressureCheckBox.setChecked(pressureBoolean);
        humidityCheckBox.setChecked(humidityBoolean);
    }
}
