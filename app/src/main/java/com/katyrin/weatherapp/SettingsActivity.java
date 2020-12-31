package com.katyrin.weatherapp;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat darkLightSwitch;
    private SwitchCompat showDetailsSwitch;
    private RadioGroup amountDayRadioGroup;
    private SwitchCompat showFeelSwitch;
    private static final String darkLightDataKey = "darkLightDataKey";
    private static final String showDetailsDataKey = "showDetailsDataKey";
    private static final String amountDayDataKey = "amountDayDataKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        darkLightSwitch = findViewById(R.id.darkLightSwitch);
        showDetailsSwitch = findViewById(R.id.showDetailsSwitch);
        amountDayRadioGroup = findViewById(R.id.amountDayRadioGroup);
        showFeelSwitch = findViewById(R.id.showFeelSwitch);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean darkLightBoolean = darkLightSwitch.isChecked();
        boolean showDetailsBoolean = showDetailsSwitch.isChecked();
        int amountDayInt = amountDayRadioGroup.getCheckedRadioButtonId();

        outState.putBoolean(darkLightDataKey, darkLightBoolean);
        outState.putBoolean(showDetailsDataKey, showDetailsBoolean);
        outState.getInt(amountDayDataKey, amountDayInt);

        DataContainer.getInstance().isShowFeelTemperature = showFeelSwitch.isChecked();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        boolean darkLightBoolean = savedInstanceState.getBoolean(darkLightDataKey);
        boolean showDetailsBoolean = savedInstanceState.getBoolean(showDetailsDataKey);
        int amountDayInt = savedInstanceState.getInt(amountDayDataKey);

        darkLightSwitch.setChecked(darkLightBoolean);
        showDetailsSwitch.setChecked(showDetailsBoolean);
        if (amountDayInt > 0) {
            ((RadioButton)amountDayRadioGroup.findViewById(amountDayInt)).setChecked(true);
        }

        showFeelSwitch.setChecked(DataContainer.getInstance().isShowFeelTemperature);
    }
}
