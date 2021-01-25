package com.katyrin.weatherapp;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.katyrin.weatherapp.fragments.AboutUsFragment;
import com.katyrin.weatherapp.fragments.CitySelectionFragment;
import com.katyrin.weatherapp.fragments.DayForecastFragment;
import com.katyrin.weatherapp.fragments.MainWeatherFragment;
import com.katyrin.weatherapp.fragments.SaveCastFragment;
import com.katyrin.weatherapp.fragments.SettingsFragment;
import com.katyrin.weatherapp.observer.Publisher;
import com.katyrin.weatherapp.observer.PublisherGetter;

public class MainActivity extends AppCompatActivity implements PublisherGetter,
        MainWeatherFragment.MainWeatherFragmentListener, CitySelectionFragment.CitySelectionFragmentListener,
        SettingsFragment.SettingsFragmentListener, DayForecastFragment.DayForecastFragmentListener {

    private final String TAG = "Life cycle: ";
    public static boolean isTabletLandscape;
    private final Publisher publisher = new Publisher();

    private CitySelectionFragment citySelectionFragment;
    private Fragment fragment;
    private FloatingActionButton citySelectionFAB;
    private BottomNavigationView navView;
    private SaveCastFragment castFragment = SaveCastFragment.getInstance();
    private DataContainer dataContainer = DataContainer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "onCreate");

        isTabletLandscape = isTabletLandscape();

        if (savedInstanceState == null) {
            firstSetDataContainer();
            onOpenMainWeatherFragment(false);
        } else {
            fragment = castFragment.fragment;
        }
        if (isTabletLandscape) {
            citySelectionFragment = (CitySelectionFragment)
                    getSupportFragmentManager().findFragmentById(R.id.leftLayout);
            CitySelectionFragment.publisher = getPublisher();
            castFragment.fragment = fragment;
            if (!(fragment instanceof MainWeatherFragment))
                fragment = new MainWeatherFragment();
            publisher.subscribe((MainWeatherFragment) fragment);
            fragment = castFragment.fragment;
        }

        citySelectionFAB = findViewById(R.id.citySelectionFAB);
        if (!MainActivity.isTabletLandscape)
            citySelectionFAB.setOnClickListener(v -> onOpenCitySelectionFragment());

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_forecast:
                    onOpenMainWeatherFragment(true);
                    return true;
                case R.id.navigation_settings:
                    openSettingsFragment();
                    return true;
                case R.id.navigation_about_us:
                    openAboutUsFragment();
                    return true;
            }
            return false;
        });

        castFragment.fragment = fragment;
    }

    private void firstSetDataContainer() {
        dataContainer.cityName = "City";
        dataContainer.dayName = "Monday";
        dataContainer.isShowDetails = true;
        dataContainer.isShowWind = true;
        dataContainer.isShowHumidity = true;
        dataContainer.isShowPressure = true;
        dataContainer.daysCount = 7;
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                dataContainer.isDarkLightCheck = true;
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                dataContainer.isDarkLightCheck = false;
                break;
        }
    }

    private void onOpenMainWeatherFragment(boolean isAddToBackStack) {
        if (fragment == null || !(fragment instanceof MainWeatherFragment)) {
            fragment = new MainWeatherFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, fragment);
            if (isAddToBackStack)
                transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        } else {
            fragment = (MainWeatherFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        }
    }

    private void openSettingsFragment() {
        if (fragment == null || !(fragment instanceof SettingsFragment)) {
            fragment = new SettingsFragment();

            Bundle args = new Bundle();
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
            castFragment.fragment = fragment;
        } else {
            fragment = (SettingsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        }
    }

    private void openAboutUsFragment() {
        if (fragment == null || !(fragment instanceof AboutUsFragment)) {
            fragment = new AboutUsFragment();

            Bundle args = new Bundle();
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();
            castFragment.fragment = fragment;
        } else {
            fragment = (SettingsFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                openSettingsFragment();
                return true;
            default:
                return false;
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
        if (isTabletLandscape)
            CitySelectionFragment.publisher = null;
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

    @Override
    public void onOpenCitySelectionFragment() {
        if (fragment == null || !(fragment instanceof CitySelectionFragment)) {
            fragment = new CitySelectionFragment();

            Bundle args = new Bundle();
            //args.putSerializable("index", getCoatContainer());
            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, fragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (!isTabletLandscape)
                transaction.addToBackStack(null);
            transaction.commit();
        } else {
            fragment = (CitySelectionFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mainContainer);
        }
    }

    @Override
    public void onScreenMainWeatherFragment() {
        fragment = new MainWeatherFragment();
    }

    @Override
    public void onScreenCitySelectionFragment() {
        if (isTabletLandscape)
            getSupportFragmentManager().popBackStack();
        else
            fragment = new CitySelectionFragment();
    }

    @Override
    public void onScreenSettingsFragment() {
        fragment = new SettingsFragment();
    }

    @Override
    public void onOpenDayForecast(String dayName, String dayTemperature, int drawableId,
                                  String cityName) {
        fragment = new DayForecastFragment();

        Bundle args = new Bundle();
        args.putString("dayName", dayName);
        args.putString("dayTemperature", dayTemperature);
        args.putInt("drawableId", drawableId);
        args.putString("cityName", cityName);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContainer, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
        castFragment.fragment = fragment;
    }

    @Override
    public void onScreenDayForecastFragment() {
        fragment = new DayForecastFragment();
    }
}