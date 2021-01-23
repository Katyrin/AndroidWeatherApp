package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.katyrin.weatherapp.DataContainer;
import com.katyrin.weatherapp.R;

public class SettingsFragment extends Fragment {

    public interface SettingsFragmentListener {
        void onScreenSettingsFragment();
    }

    private SwitchMaterial showDetailsSwitch;
    private CheckBox windCheckBox;
    private CheckBox humidityCheckBox;
    private CheckBox pressureCheckBox;
    private RadioButton threeRadioButton;
    private RadioButton fiveRadioButton;
    private RadioButton sevenRadioButton;

    private SettingsFragmentListener listener;
    private DataContainer dataContainer = DataContainer.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        listener.onScreenSettingsFragment();
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setDataContainer();
        setAction();

        setViewVisibility(dataContainer.isShowDetails);
    }

    private void initViews(View view) {
        showDetailsSwitch = view.findViewById(R.id.showDetailsSwitch);
        windCheckBox = view.findViewById(R.id.windCheckBox);
        humidityCheckBox = view.findViewById(R.id.humidityCheckBox);
        pressureCheckBox = view.findViewById(R.id.pressureCheckBox);
        threeRadioButton = view.findViewById(R.id.threeRadioButton);
        fiveRadioButton = view.findViewById(R.id.fiveRadioButton);
        sevenRadioButton = view.findViewById(R.id.sevenRadioButton);
    }

    private void setDataContainer() {
        showDetailsSwitch.setChecked(dataContainer.isShowDetails);
        windCheckBox.setChecked(dataContainer.isShowWind);
        humidityCheckBox.setChecked(dataContainer.isShowHumidity);
        pressureCheckBox.setChecked(dataContainer.isShowPressure);
        switch (dataContainer.daysCount) {
            case 3:
                threeRadioButton.setChecked(true);
                return;
            case 5:
                fiveRadioButton.setChecked(true);
                return;
            case 7:
                sevenRadioButton.setChecked(true);
        }
    }

    private void setAction() {
        showDetailsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setViewVisibility(isChecked);
            }
        });
    }

    private void setViewVisibility(boolean isChecked) {
        if (isChecked) {
            windCheckBox.setVisibility(View.VISIBLE);
            humidityCheckBox.setVisibility(View.VISIBLE);
            pressureCheckBox.setVisibility(View.VISIBLE);
        } else {
            windCheckBox.setVisibility(View.GONE);
            humidityCheckBox.setVisibility(View.GONE);
            pressureCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dataContainer.isShowDetails = showDetailsSwitch.isChecked();
        dataContainer.isShowWind = windCheckBox.isChecked();
        dataContainer.isShowHumidity = humidityCheckBox.isChecked();
        dataContainer.isShowPressure = pressureCheckBox.isChecked();
        if (threeRadioButton.isChecked()){
            dataContainer.daysCount = 3;
        } else if (fiveRadioButton.isChecked()){
            dataContainer.daysCount = 5;
        } else if (sevenRadioButton.isChecked()){
            dataContainer.daysCount = 7;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (SettingsFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}