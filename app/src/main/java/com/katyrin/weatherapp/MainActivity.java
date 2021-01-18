package com.katyrin.weatherapp;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.katyrin.weatherapp.fragments.MainWeatherFragment;
import com.katyrin.weatherapp.observer.Publisher;
import com.katyrin.weatherapp.observer.PublisherGetter;

public class MainActivity extends AppCompatActivity implements PublisherGetter {

    private final String TAG = "Life cycle: ";
    public static boolean isTabletLandscape;
    private final Publisher publisher = new Publisher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        isTabletLandscape = isTabletLandscape();

        MainWeatherFragment mainWeatherFragment =
                (MainWeatherFragment) getSupportFragmentManager().findFragmentById(R.id.leftLayout);
        publisher.subscribe(mainWeatherFragment);
    }

    private boolean isTabletLandscape() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x / (int)getResources().getDisplayMetrics().density;
        int height = size.y / (int)getResources().getDisplayMetrics().density;
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE &&
                height > 700 && width > 700;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int countOfFragmentInManager = getSupportFragmentManager().getBackStackEntryCount();
        if(countOfFragmentInManager > 0) {
            getSupportFragmentManager().popBackStack();
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
    public Publisher getPublisher() {
        return publisher;
    }
}