package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.katyrin.weatherapp.IRVCitiesOnItemClick;
import com.katyrin.weatherapp.R;
import com.katyrin.weatherapp.RecyclerCitiesAdapter;
import com.katyrin.weatherapp.observer.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CitySelectionFragment extends Fragment implements IRVCitiesOnItemClick {

    public interface CitySelectionFragmentListener {
        void onScreenCitySelectionFragment();
        void showForecast(String cityName);
    }

    private RecyclerView citiesRV;
    private RecyclerCitiesAdapter adapter;
    private MaterialButton showForecastButton;
    private TextInputEditText enterCityEditText;

    public static Publisher publisher;
    private CitySelectionFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        listener.onScreenCitySelectionFragment();
        return inflater.inflate(R.layout.fragment_city_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRV();
        setAction();
    }

    private void initViews(View view) {
        citiesRV = view.findViewById(R.id.citiesRV);
        enterCityEditText = view.findViewById(R.id.enterCityEditText);
        showForecastButton = view.findViewById(R.id.showForecastButton);
    }

    private void setupRV() {
        ArrayList<String> listCities = new ArrayList<>(Arrays.asList(
                getString(R.string.novosibirsk), getString(R.string.moscow), getString(R.string.sochi),
                getString(R.string.paris), getString(R.string.milan), getString(R.string.omsk),
                getString(R.string.novgorod), getString(R.string.krasnoyarsk),
                getString(R.string.erevan), getString(R.string.dubai)));
        LinearLayoutManager linearLayout = new LinearLayoutManager(requireContext());
        adapter = new RecyclerCitiesAdapter(listCities, this);

        citiesRV.setLayoutManager(linearLayout);
        citiesRV.setAdapter(adapter);
    }

    private void setAction() {
        showForecastButton.setOnClickListener(onSaveSelection);
        enterCityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextView tv = (TextView) v;
                validate(tv, getString(R.string.enter_city_name));
            }
        });

        InputMethodManager inputManager =
                (InputMethodManager) requireContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

        enterCityEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                inputManager.hideSoftInputFromWindow(
                        requireActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                enterCityEditText.clearFocus();
            }
            return true;
        });
    }

    private void validate(TextView tv, String message) {
        if (tv.getText().toString().equals(""))
            tv.setError(message);
        else
            tv.setError(null);
    }



    View.OnClickListener onSaveSelection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = Objects.requireNonNull(enterCityEditText.getText()).toString();
            if (cityName.equals("")) {
                Snackbar.make(requireActivity().findViewById(R.id.mainLayout), R.string.enter_city,
                        Snackbar.LENGTH_LONG).setAnchorView(R.id.nav_view).show();
            } else {
                listener.showForecast(cityName);
                setCityName(cityName);
            }
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (CitySelectionFragmentListener) context;
    }

    private boolean isTabletLandscape() {
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x / (int)getResources().getDisplayMetrics().density;
        int height = size.y / (int)getResources().getDisplayMetrics().density;
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_LANDSCAPE &&
                height > 700 && width > 700;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onItemCityClicked(String cityName) {
        setCityName(cityName);
        listener.showForecast(cityName);
    }

    private void setCityName(String cityName) {
        if (isTabletLandscape()) {
            publisher.notify(cityName);
        }
    }
}