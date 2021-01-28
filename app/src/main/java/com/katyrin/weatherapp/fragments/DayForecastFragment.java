package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.katyrin.weatherapp.DataContainer;
import com.katyrin.weatherapp.R;

public class DayForecastFragment extends Fragment {
    public interface DayForecastFragmentListener {
        void onScreenDayForecastFragment();
    }

    private TextView dayName;
    private TextView dayTemperature;
    private ImageView dayImageView;
    private TextView cityName;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private ImageView windImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
    private DayForecastFragmentListener listener;

    private boolean isShowWind = true;
    private boolean isShowHumidity = true;
    private boolean isShowPressure = true;
    private DataContainer dataContainer = DataContainer.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        listener.onScreenDayForecastFragment();
        return inflater.inflate(R.layout.fragment_day_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        dayName.setText(getArguments().getString("dayName"));
        dayTemperature.setText(getArguments().getString("dayTemperature"));
        dayImageView.setImageDrawable(ContextCompat.getDrawable(requireActivity().getApplicationContext(),
                getArguments().getInt("drawableId")));
        cityName.setText(getArguments().getString("cityName"));

        setSettings();

        if (isShowWind)
            windTextView.setText(getArguments().getString("wind"));
        if (isShowHumidity)
            humidityTextView.setText(getArguments().getString("humidity"));
        if (isShowPressure)
            pressureTextView.setText(getArguments().getString("pressure"));
    }

    private void initViews(View view) {
        dayName = view.findViewById(R.id.dayName);
        dayTemperature = view.findViewById(R.id.dayTemperature);
        dayImageView = view.findViewById(R.id.dayImageView);
        cityName = view.findViewById(R.id.cityName);

        windTextView = view.findViewById(R.id.windTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        windImageView = view.findViewById(R.id.windImageView);
        humidityImageView = view.findViewById(R.id.humidityImageView);
        pressureImageView = view.findViewById(R.id.pressureImageView);
    }

    private void setSettings() {
        if (dataContainer.isShowDetails){
            isShowWind = dataContainer.isShowWind;
            isShowHumidity = dataContainer.isShowHumidity;
            isShowPressure = dataContainer.isShowPressure;
        } else {
            isShowWind = false;
            isShowHumidity = false;
            isShowPressure = false;
        }
        showSelectedOptions();
    }

    private void showSelectedOptions() {
        if (isShowWind) {
            windImageView.setVisibility(View.VISIBLE);
            windTextView.setVisibility(View.VISIBLE);
        } else {
            windTextView.setVisibility(View.GONE);
            windImageView.setVisibility(View.GONE);
        }

        if (isShowHumidity) {
            humidityImageView.setVisibility(View.VISIBLE);
            humidityTextView.setVisibility(View.VISIBLE);
        } else {
            humidityImageView.setVisibility(View.GONE);
            humidityTextView.setVisibility(View.GONE);
        }

        if (isShowPressure) {
            pressureImageView.setVisibility(View.VISIBLE);
            pressureTextView.setVisibility(View.VISIBLE);
        } else {
            pressureImageView.setVisibility(View.GONE);
            pressureTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (DayForecastFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
