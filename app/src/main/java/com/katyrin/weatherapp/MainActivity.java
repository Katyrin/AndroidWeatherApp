package com.katyrin.weatherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Life cycle: ";
    private TextView cityTextView;
    private TextView temperatureTextView;
    private ImageView weatherImageView;
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

    static String cityKey = "cityKey";
    static String windKey = "windKey";
    static String humidityKey = "humidityKey";
    static String pressureKey = "pressureKey";
    private boolean isShowWind;
    private boolean isShowHumidity;
    private boolean isShowPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        cityTextView = findViewById(R.id.cityTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        windTextView = findViewById(R.id.windTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);
        windImageView = findViewById(R.id.windImageView);
        humidityImageView = findViewById(R.id.humidityImageView);
        pressureImageView = findViewById(R.id.pressureImageView);
        cityInformationImageButton = findViewById(R.id.cityInformationImageButton);

        cityInformationImageButton.setOnClickListener(onCityInfoListener);

        citySelectionFAB = findViewById(R.id.citySelectionFAB);
        citySelectionFAB.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CitySelectionActivity.class);
            startActivity(intent);
                });

        String cityName = getIntent().getStringExtra(cityKey);
        if (cityName != null){
            cityTextView.setText(cityName);
            isShowWind = getIntent().getBooleanExtra(windKey, false);
            isShowHumidity = getIntent().getBooleanExtra(humidityKey, false);
            isShowPressure = getIntent().getBooleanExtra(pressureKey, false);
            showSelectedOptions();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
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
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

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

    private View.OnClickListener onCityInfoListener = new View.OnClickListener() {
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
}