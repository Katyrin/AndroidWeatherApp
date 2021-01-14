package com.katyrin.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CitySelectionActivity extends AppCompatActivity {

    private CheckBox windCheckBox;
    private CheckBox pressureCheckBox;
    private CheckBox humidityCheckBox;
    private static final String windDataKey = "windDataKey";
    private static final String pressureDataKey = "pressureDataKey";
    private static final String humidityDataKey = "humidityDataKey";

    private FloatingActionButton saveCitySettingsFAB;
    private TextInputEditText enterCityEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_selection_activity);

        windCheckBox = findViewById(R.id.windCheckBox);
        pressureCheckBox = findViewById(R.id.pressureCheckBox);
        humidityCheckBox = findViewById(R.id.humidityCheckBox);

        enterCityEditText = findViewById(R.id.enterCityEditText);
        saveCitySettingsFAB = findViewById(R.id.saveCitySettingsFAB);
        saveCitySettingsFAB.setOnClickListener(onSaveSelection);
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

    View.OnClickListener onSaveSelection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = Objects.requireNonNull(enterCityEditText.getText()).toString();
            if (cityName.equals("")) {
                Snackbar.make(findViewById(R.id.citySelectionLayout), R.string.enter_city,
                        Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(CitySelectionActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.cityKey, cityName);
                intent.putExtra(MainActivity.windKey, windCheckBox.isChecked());
                intent.putExtra(MainActivity.humidityKey, humidityCheckBox.isChecked());
                intent.putExtra(MainActivity.pressureKey, pressureCheckBox.isChecked());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
}
