package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.katyrin.weatherapp.DataContainer;
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
    }

    private RecyclerView citiesRV;
    private RecyclerCitiesAdapter adapter;

    private FloatingActionButton saveCitySettingsFAB;
    private TextInputEditText enterCityEditText;
    public static Publisher publisher;
    private CitySelectionFragmentListener listener;
    private DataContainer dataContainer = DataContainer.getInstance();

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
        saveCitySettingsFAB = view.findViewById(R.id.saveCitySettingsFAB);
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
        saveCitySettingsFAB.setOnClickListener(onSaveSelection);
    }



    View.OnClickListener onSaveSelection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = Objects.requireNonNull(enterCityEditText.getText()).toString();
            if (cityName.equals("")) {
                Snackbar.make(requireActivity().findViewById(R.id.citySelectionLayout), R.string.enter_city,
                        Snackbar.LENGTH_LONG).show();
            } else {
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
    }

    private void setCityName(String cityName) {
        requireActivity().getSupportFragmentManager().popBackStack();
        dataContainer.cityName = cityName;
        if (isTabletLandscape()) {
            publisher.notify(cityName);
        }
    }
}