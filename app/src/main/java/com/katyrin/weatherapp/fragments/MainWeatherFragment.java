package com.katyrin.weatherapp.fragments;

import android.content.Context;
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
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.katyrin.weatherapp.CoatContainer;
import com.katyrin.weatherapp.DataContainer;
import com.katyrin.weatherapp.DataRVClass;
import com.katyrin.weatherapp.IRVDaysOnItemClick;
import com.katyrin.weatherapp.ItemDivider;
import com.katyrin.weatherapp.R;
import com.katyrin.weatherapp.RecyclerDaysAdapter;
import com.katyrin.weatherapp.observer.Observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class MainWeatherFragment extends Fragment implements Observer, IRVDaysOnItemClick {

    public interface MainWeatherFragmentListener {
        void onOpenCitySelectionFragment();
        void onScreenMainWeatherFragment();
        void onOpenDayForecast(String dayName, String dayTemperature, int drawableId,
                               String cityName, String wind, String humidity, String pressure);
    }

    private static TextView cityTextView;
    private TextView temperatureTextView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private ImageView windImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
    private ImageButton cityInformationImageButton;
    private ImageView weatherImageView;
    private TextView dayTextView;

    private RecyclerView daysRV;
    private RecyclerDaysAdapter adapter;
    private MainWeatherFragmentListener listener;

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
    private final DataContainer dataContainer = DataContainer.getInstance();
    private static boolean isOnScreen;

    public static Map<String,Integer> icons = new ArrayMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        listener.onScreenMainWeatherFragment();
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isOnScreen = false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillIcons();
        initViews(view);
        setupDaysRV();
        isOnScreen = true;
        setSettings();
    }

    private static void fillIcons() {
        icons.put("01d", R.drawable.i01d);
        icons.put("01n", R.drawable.i01n);
        icons.put("02d", R.drawable.i02d);
        icons.put("02n", R.drawable.i02n);
        icons.put("03d", R.drawable.i03d);
        icons.put("03n", R.drawable.i03n);
        icons.put("04d", R.drawable.i04d);
        icons.put("04n", R.drawable.i04n);
        icons.put("09d", R.drawable.i09d);
        icons.put("09n", R.drawable.i09n);
        icons.put("10d", R.drawable.i10d);
        icons.put("10n", R.drawable.i10n);
        icons.put("11d", R.drawable.i11d);
        icons.put("11n", R.drawable.i11n);
        icons.put("13d", R.drawable.i13d);
        icons.put("13n", R.drawable.i13n);
        icons.put("50d", R.drawable.i50d);
        icons.put("50n", R.drawable.i50n);
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

    @Override
    public void onResume() {
        super.onResume();
        cityTextView.setText(dataContainer.cityName);
    }

    @Override
    public void onStart() {
        super.onStart();
        setActionsViews();
    }

    private void initViews(View view) {
        cityTextView = view.findViewById(R.id.cityName);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        windTextView = view.findViewById(R.id.windTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        windImageView = view.findViewById(R.id.windImageView);
        humidityImageView = view.findViewById(R.id.humidityImageView);
        pressureImageView = view.findViewById(R.id.pressureImageView);
        cityInformationImageButton = view.findViewById(R.id.cityInformationImageButton);
        daysRV = view.findViewById(R.id.daysRV);
        weatherImageView = view.findViewById(R.id.weatherImageView);
        dayTextView = view.findViewById(R.id.dayTextView);

        cityTextView.setText(dataContainer.cityName);
        temperatureTextView.setText(dataContainer.currentTemperature);
        windTextView.setText(dataContainer.currentWind);
        humidityTextView.setText(dataContainer.currentHumidity);
        pressureTextView.setText(dataContainer.currentPressure);

        if (dataContainer.currentIcon != null) {
            int icon = icons.get(dataContainer.currentIcon);
            weatherImageView.setImageResource(icon);
        }
        if (dataContainer.dt != null) {
            dayTextView.setText(dataContainer.dt);
        }
    }

    private void setupDaysRV() {
        if (dataContainer.dataRVClasses != null) {
            DataRVClass[] dataRVClass = dataContainer.dataRVClasses;
            ArrayList<DataRVClass> list = new ArrayList<>(dataRVClass.length);
            list.addAll(Arrays.asList(dataRVClass).subList(0, dataContainer.daysCount));

            LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext());
            adapter = new RecyclerDaysAdapter(list, this, requireContext());

            ItemDivider itemDecoration = new ItemDivider(requireActivity().getBaseContext(),
                    LinearLayoutManager.VERTICAL, Objects.requireNonNull(ContextCompat.getDrawable(
                    getActivity().getBaseContext(), R.drawable.decorator_item)));

            daysRV.addItemDecoration(itemDecoration);
            daysRV.setLayoutManager(linearLayout);
            daysRV.setAdapter(adapter);
        }
    }

    private void setActionsViews() {
        cityInformationImageButton.setOnClickListener(onCityInfoListener);
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
        if (isOnScreen)
            cityTextView.setText(cityName);
        else
            dataContainer.cityName = cityName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                assert data != null;
                CoatContainer coatContainer = (CoatContainer) data.getSerializableExtra("returnData");
                cityTextView.setText(getCityName(coatContainer));
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

    @Override
    public void onItemClicked(String dayName, String dayTemperature, int drawableId,
                              String wind, String humidity, String pressure) {
        listener.onOpenDayForecast(dayName, dayTemperature, drawableId, dataContainer.cityName,
                wind, humidity, pressure);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (MainWeatherFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}