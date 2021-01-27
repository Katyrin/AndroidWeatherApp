package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.katyrin.weatherapp.DataContainer;
import com.katyrin.weatherapp.R;

public class SettingsFragment extends Fragment {

    public interface SettingsFragmentListener {
        void onScreenSettingsFragment();
    }

    private SwitchMaterial showDetailsSwitch;
    private MaterialCheckBox windCheckBox;
    private MaterialCheckBox humidityCheckBox;
    private MaterialCheckBox pressureCheckBox;
    private MaterialRadioButton threeRadioButton;
    private MaterialRadioButton fiveRadioButton;
    private MaterialRadioButton sevenRadioButton;
    private MaterialButton cancelButton;
    private MaterialButton saveButton;
    private SwitchMaterial darkLightSwitch;

    private SettingsFragmentListener listener;
    private final DataContainer dataContainer = DataContainer.getInstance();

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
        cancelButton = view.findViewById(R.id.cancelButton);
        saveButton = view.findViewById(R.id.saveButton);
        darkLightSwitch = view.findViewById(R.id.darkLightSwitch);
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
        darkLightSwitch.setChecked(dataContainer.isDarkLightCheck);
    }

    private void setAction() {
        showDetailsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setViewVisibility(isChecked));

        cancelButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        saveButton.setOnClickListener(v ->
                Snackbar.make(requireActivity().findViewById(R.id.settingsLayout), R.string.confirmation_save,
                Snackbar.LENGTH_LONG).setAction( android.R.string.ok, v1 -> {
                    requireActivity().getSupportFragmentManager().popBackStack();

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
                    dataContainer.isDarkLightCheck = darkLightSwitch.isChecked();
                    setTheme(darkLightSwitch.isChecked());
                    requireActivity().recreate();
                }).setAnchorView(R.id.nav_view).show());
    }

    private void setTheme(boolean isChecked) {
        if (isChecked)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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