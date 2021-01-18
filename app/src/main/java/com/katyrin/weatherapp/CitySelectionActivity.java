package com.katyrin.weatherapp;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.katyrin.weatherapp.fragments.CitySelectionFragment;

public class CitySelectionActivity extends AppCompatActivity {
    public static boolean isTabletLandscape;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);

        isTabletLandscape = isTabletLandscape();

        if (isTabletLandscape){
            finish();
            return;
        }

        if (savedInstanceState == null) {
            CitySelectionFragment citySelectionFragment = new CitySelectionFragment();
            citySelectionFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, citySelectionFragment).commit();
        }
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
}
