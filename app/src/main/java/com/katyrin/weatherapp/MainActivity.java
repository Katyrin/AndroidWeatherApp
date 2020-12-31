package com.katyrin.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Life cycle: ";
    private TextView cityTextView;
    private TextView temperatureTextView;
    private ImageView weatherImageView;
    private TextView windTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private static final String cityDataKey = "cityDataKey";
    private static final String temperatureDataKey = "temperatureDataKey";
    private static final String weatherImageDataKey = "weatherImageDataKey";
    private static final String windDataKey = "windDataKey";
    private static final String humidityDataKey = "humidityDataKey";
    private static final String pressureDataKey = "pressureDataKey";

    private ImageButton addCityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate");

        cityTextView = findViewById(R.id.cityTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        windTextView = findViewById(R.id.windTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        pressureTextView = findViewById(R.id.pressureTextView);

        addCityButton = findViewById(R.id.addCityButton);
        addCityButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String cityTxt = cityTextView.getText().toString();
        String temperatureTxt = temperatureTextView.getText().toString();
        Bitmap weatherImageBitmap = ((BitmapDrawable)weatherImageView.getDrawable()).getBitmap();
        String windTxt = windTextView.getText().toString();
        String humidityTxt = humidityTextView.getText().toString();
        String pressureTxt = pressureTextView.getText().toString();

        outState.putString(cityDataKey, cityTxt);
        outState.putString(temperatureDataKey, temperatureTxt);
        outState.putParcelable(weatherImageDataKey, weatherImageBitmap);
        outState.putString(windDataKey, windTxt);
        outState.putString(humidityDataKey, humidityTxt);
        outState.putString(pressureDataKey, pressureTxt);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String cityTxt = savedInstanceState.getString(cityDataKey);
        String temperatureTxt = savedInstanceState.getString(temperatureDataKey);
        Bitmap weatherImageBitmap = (Bitmap) savedInstanceState.getParcelable(weatherImageDataKey);
        String windTxt = savedInstanceState.getString(windDataKey);
        String humidityTxt = savedInstanceState.getString(humidityDataKey);
        String pressureTxt = savedInstanceState.getString(pressureDataKey);

        cityTextView.setText(cityTxt);
        temperatureTextView.setText(temperatureTxt);
        weatherImageView.setImageBitmap(weatherImageBitmap);
        windTextView.setText(windTxt);
        humidityTextView.setText(humidityTxt);
        pressureTextView.setText(pressureTxt);
    }
}