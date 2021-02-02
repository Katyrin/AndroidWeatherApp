package com.katyrin.weatherapp;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.katyrin.weatherapp.fragments.AboutUsFragment;
import com.katyrin.weatherapp.fragments.CitySelectionFragment;
import com.katyrin.weatherapp.fragments.DayForecastFragment;
import com.katyrin.weatherapp.fragments.MainWeatherFragment;
import com.katyrin.weatherapp.fragments.MyBottomSheetDialogFragment;
import com.katyrin.weatherapp.fragments.SaveCastFragment;
import com.katyrin.weatherapp.fragments.SettingsFragment;
import com.katyrin.weatherapp.model.WeatherRequest;
import com.katyrin.weatherapp.observer.Publisher;
import com.katyrin.weatherapp.observer.PublisherGetter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PublisherGetter,
        MainWeatherFragment.MainWeatherFragmentListener, CitySelectionFragment.CitySelectionFragmentListener,
        SettingsFragment.SettingsFragmentListener, DayForecastFragment.DayForecastFragmentListener,
        Request.RequestListener {

    public static boolean isTabletLandscape;
    private final Publisher publisher = new Publisher();

    private CitySelectionFragment citySelectionFragment;
    private Fragment fragment;
    private FloatingActionButton citySelectionFAB;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private View headerView;
    private final SaveCastFragment castFragment = SaveCastFragment.getInstance();
    private final DataContainer dataContainer = DataContainer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isTabletLandscape = isTabletLandscape();
        initNavigationMenu();
        updateNavView();

        if (savedInstanceState == null) {
            firstSetDataContainer();
            onOpenMainWeatherFragment(false);
            if (isNetworkAvailable())
                new Request("Novosibirsk", this);
            else
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_LONG).show();
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

        castFragment.fragment = fragment;
    }

    private void initNavigationMenu() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case (R.id.navigation_forecast): {
                    onOpenMainWeatherFragment(true);
                    break;
                }
                case (R.id.navigation_settings): {
                    openSettingsFragment();
                    break;
                }
                case (R.id.navigation_about_us): {
                    openAboutUsFragment();
                    break;
                }
            }
            drawerLayout.close();
            return false;
        });
    }

    private void firstSetDataContainer() {
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
        navigationView.setCheckedItem(R.id.navigation_forecast);
        if (fragment == null || !(fragment instanceof MainWeatherFragment)) {
            fragment = new MainWeatherFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainer, fragment, "MainWeatherFragment");
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
        navigationView.setCheckedItem(R.id.navigation_settings);
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
        navigationView.setCheckedItem(R.id.navigation_forecast);
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
            fragment = (AboutUsFragment)
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
            case (R.id.settings):
                openSettingsFragment();
                return true;
            case (R.id.search):
                openSearchDialogFragment();
            default:
                return false;
        }
    }

    private void openSearchDialogFragment() {
        MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();
        dialogFragment.setMyBottomSheetDialogFragmentListener(dialogListener);
        dialogFragment.show(getSupportFragmentManager(),"dialog_fragment");
    }

    MyBottomSheetDialogFragment.MyBottomSheetDialogFragmentListener dialogListener = (cityName) -> {
        backToMainFragment();
        new Request(cityName, this);
            };

    private void backToMainFragment() {
        int stackSize = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < stackSize; i++)
            getSupportFragmentManager().popBackStack();
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
    protected void onStop() {
        super.onStop();
        if (isTabletLandscape)
            CitySelectionFragment.publisher = null;
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
    public void showForecast(String cityName) {
        if (isNetworkAvailable()) {
            backToMainFragment();
            new Request(cityName, this);
        }
        else
            Snackbar.make(findViewById(R.id.mainLayout), getString(R.string.no_internet),
                    Snackbar.LENGTH_LONG).setAnchorView(R.id.nav_view).show();
    }

    @Override
    public void onScreenSettingsFragment() {
        fragment = new SettingsFragment();
    }

    @Override
    public void onOpenDayForecast(String dayName, String dayTemperature, int drawableId,
                                  String cityName, String wind, String humidity, String pressure) {
        fragment = new DayForecastFragment();

        Bundle args = new Bundle();
        args.putString("dayName", dayName);
        args.putString("dayTemperature", dayTemperature);
        args.putInt("drawableId", drawableId);
        args.putString("cityName", cityName);
        args.putString("wind", wind);
        args.putString("humidity", humidity);
        args.putString("pressure", pressure);
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

    @Override
    public void requestListenerCallBack(int i, Handler handler, WeatherRequest weatherRequest) {
        handler.post(() -> {
            if (i != 1) {
                displayWatcherRV(weatherRequest);
            } else {
                displayWeather(weatherRequest);
            }
        });
    }

    private void displayWeather(WeatherRequest weatherRequest) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);

        String cityName = weatherRequest.getName();
        String currentTemperature = numberFormat.format(weatherRequest.getMain().getTemp())
                + getString(R.string.t);
        String currentPressure = numberFormat.format(weatherRequest.getMain().getPressure())
                + getString(R.string.pressure);
        String currentHumidity = numberFormat.format(weatherRequest.getMain().getHumidity())
                + getString(R.string.percent);
        String currentWind = numberFormat.format(weatherRequest.getWind().getSpeed())
                + getString(R.string.speed);
        String currentIcon = weatherRequest.getWeather()[0].getIcon();
        String description = weatherRequest.getWeather()[0].getDescription();

        dataContainer.cityName = cityName;
        dataContainer.currentTemperature = currentTemperature;
        dataContainer.currentPressure = currentPressure;
        dataContainer.currentHumidity = currentHumidity;
        dataContainer.currentWind = currentWind;
        dataContainer.currentIcon = currentIcon;
        dataContainer.dt = convertTimeStampToDay(weatherRequest.getDt());
        dataContainer.currentDescription = description;
        new Request(weatherRequest.getCoord().getLat(), weatherRequest.getCoord().getLon(), this);

        updateNavView();
    }

    private void updateNavView() {
        TextView temperatureTextView = headerView.findViewById(R.id.temperatureTextView);
        TextView descriptionTextView = headerView.findViewById(R.id.descriptionTextView);
        ImageView weatherImageView = headerView.findViewById(R.id.weatherImageView);

        temperatureTextView.setText(dataContainer.currentTemperature);
        descriptionTextView.setText(dataContainer.currentDescription);
        if (dataContainer.currentIcon != null) {
            int icon = MainWeatherFragment.icons.get(dataContainer.currentIcon);
            weatherImageView.setImageResource(icon);
        }
    }

    private void displayWatcherRV(WeatherRequest weatherRequest) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(1);

        DataRVClass[] dataRVClasses = new DataRVClass[weatherRequest.getDaily().length];

        for (int i = 0; i < weatherRequest.getDaily().length; i++) {
            dataRVClasses[i] = new DataRVClass(
                    convertTimeStampToDay(weatherRequest.getDaily()[i].getDt()),
                    numberFormat.format(weatherRequest.getDaily()[i].getTemp().getDay())
                            + getString(R.string.t),
                    weatherRequest.getDaily()[i].getWeather()[0].getIcon(),
                    numberFormat.format(weatherRequest.getDaily()[i].getWind_speed())
                            + getString(R.string.speed),
                    numberFormat.format(weatherRequest.getDaily()[i].getHumidity())
                            + getString(R.string.percent),
                    numberFormat.format(weatherRequest.getDaily()[i].getPressure())
                            + getString(R.string.pressure)
            );
        }

        dataContainer.dataRVClasses = dataRVClasses;

        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("MainWeatherFragment");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

    private String convertTimeStampToDay(long timeStamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp * 1000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        String result = dateFormat.format(calendar.getTime());
        StringBuilder sb = new StringBuilder(result);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        int stackSize = getSupportFragmentManager().getBackStackEntryCount();
        if (stackSize > 0) {
            backToMainFragment();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.attention).setMessage(R.string.close_message)
                    .setPositiveButton(R.string.cancel, (dialogInterface, i) ->
                            dialogInterface.dismiss())
                    .setNegativeButton(R.string.exit, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        super.onBackPressed();
                    }).create().show();
        }
    }
}