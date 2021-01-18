package com.katyrin.weatherapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.katyrin.weatherapp.CitySelectionActivity;
import com.katyrin.weatherapp.CoatContainer;
import com.katyrin.weatherapp.MainActivity;
import com.katyrin.weatherapp.R;
import com.katyrin.weatherapp.observer.Observer;

import static android.app.Activity.RESULT_OK;

public class MainWeatherFragment extends Fragment implements Observer {

    private TextView cityTextView;
    private TextView temperatureTextView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private ImageView windImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
    private FloatingActionButton citySelectionFAB;
    private ImageButton cityInformationImageButton;

    private static final String cityDataKey = "cityDataKey";
    private static final String temperatureDataKey = "temperatureDataKey";
    private static final String windDataKey = "windDataKey";
    private static final String humidityDataKey = "humidityDataKey";
    private static final String pressureDataKey = "pressureDataKey";

    static String windKey = "windKey";
    static String humidityKey = "humidityKey";
    static String pressureKey = "pressureKey";
    private boolean isShowWind = true;
    private boolean isShowHumidity = true;
    private boolean isShowPressure = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        setActionsViews();
    }

    private void initViews(View view) {
        cityTextView = view.findViewById(R.id.cityTextView);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        windTextView = view.findViewById(R.id.windTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        windImageView = view.findViewById(R.id.windImageView);
        humidityImageView = view.findViewById(R.id.humidityImageView);
        pressureImageView = view.findViewById(R.id.pressureImageView);
        cityInformationImageButton = view.findViewById(R.id.cityInformationImageButton);
        citySelectionFAB = view.findViewById(R.id.citySelectionFAB);
    }

    private void setActionsViews() {
        cityInformationImageButton.setOnClickListener(onCityInfoListener);
        if (MainActivity.isTabletLandscape)
            citySelectionFAB.setVisibility(View.GONE);
        else
            citySelectionFAB.setVisibility(View.VISIBLE);
        citySelectionFAB.setOnClickListener(v -> showCitySelectionFragment());
    }

    private final View.OnClickListener onCityInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = cityTextView.getText().toString();
            if (!cityName.equals("")){
                String urlStr = getString(R.string.wiki_url) + cityName;
                Uri uri = Uri.parse(urlStr);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(webIntent);
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String cityTxt = cityTextView.getText().toString();
        String temperatureTxt = temperatureTextView.getText().toString();
        String windTxt = windTextView.getText().toString();
        String humidityTxt = humidityTextView.getText().toString();
        String pressureTxt = pressureTextView.getText().toString();

        outState.putString(cityDataKey, cityTxt);
        outState.putString(temperatureDataKey, temperatureTxt);
        outState.putString(windDataKey, windTxt);
        outState.putString(humidityDataKey, humidityTxt);
        outState.putString(pressureDataKey, pressureTxt);

        outState.putBoolean(windKey, isShowWind);
        outState.putBoolean(humidityKey, isShowHumidity);
        outState.putBoolean(pressureKey, isShowPressure);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            String cityTxt = savedInstanceState.getString(cityDataKey);
            String temperatureTxt = savedInstanceState.getString(temperatureDataKey);
            String windTxt = savedInstanceState.getString(windDataKey);
            String humidityTxt = savedInstanceState.getString(humidityDataKey);
            String pressureTxt = savedInstanceState.getString(pressureDataKey);

            cityTextView.setText(cityTxt);
            temperatureTextView.setText(temperatureTxt);
            windTextView.setText(windTxt);
            humidityTextView.setText(humidityTxt);
            pressureTextView.setText(pressureTxt);

            isShowWind = savedInstanceState.getBoolean(windKey);
            isShowHumidity = savedInstanceState.getBoolean(humidityKey);
            isShowPressure = savedInstanceState.getBoolean(pressureKey);
            showSelectedOptions();
        }

        if (MainActivity.isTabletLandscape) {
            showCitySelectionFragment();
        }
    }

    private void showCitySelectionFragment() {
        if (MainActivity.isTabletLandscape) {
            assert getFragmentManager() != null;
            CitySelectionFragment citySelectionFragment =
                    (CitySelectionFragment) getFragmentManager().findFragmentById(R.id.rightLLL);
            if (citySelectionFragment == null) {
                citySelectionFragment = new CitySelectionFragment();

                Bundle args = new Bundle();
                args.putSerializable("index", getCoatContainer());
                citySelectionFragment.setArguments(args);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.rightLLL, citySelectionFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack("SomeKey");
                transaction.commit();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CitySelectionActivity.class);
            intent.putExtra("index", getCoatContainer());
            startActivityForResult(intent, 1);
        }
    }

    private CoatContainer getCoatContainer() {
        CoatContainer container = new CoatContainer();
        container.cityName = cityTextView.getText().toString();
        container.isShowWind = isShowWind;
        container.isShowHumidity = isShowHumidity;
        container.isShowPressure = isShowPressure;
        return container;
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
    public void updateCityName(String cityName) {
        cityTextView.setText(cityName);
    }

    @Override
    public void updateShowDetails(boolean isWindy, boolean isHumidity, boolean isPressure) {
        isShowWind = isWindy;
        isShowHumidity = isHumidity;
        isShowPressure = isPressure;
        showSelectedOptions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                assert data != null;
                CoatContainer coatContainer = (CoatContainer) data.getSerializableExtra("returnData");
                cityTextView.setText(getCityName(coatContainer));
                isShowWind = getCheckWind(coatContainer);
                isShowHumidity = getCheckHumidity(coatContainer);
                isShowPressure = getCheckPressure(coatContainer);
                showSelectedOptions();
            }
        }
    }

    private String getCityName(CoatContainer coatContainer) {
        try {
            return coatContainer.cityName;
        } catch (Exception e) {
            return "";
        }
    }

    private boolean getCheckWind(CoatContainer coatContainer) {
        try {
            return coatContainer.isShowWind;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean getCheckHumidity(CoatContainer coatContainer) {
        try {
            return coatContainer.isShowHumidity;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean getCheckPressure(CoatContainer coatContainer) {
        try {
            return coatContainer.isShowPressure;
        } catch (Exception e) {
            return true;
        }
    }
}
