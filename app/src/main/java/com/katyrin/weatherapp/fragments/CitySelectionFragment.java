package com.katyrin.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.katyrin.weatherapp.CoatContainer;
import com.katyrin.weatherapp.MainActivity;
import com.katyrin.weatherapp.R;
import com.katyrin.weatherapp.observer.Publisher;
import com.katyrin.weatherapp.observer.PublisherGetter;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CitySelectionFragment extends Fragment {

    private TextView novosibirskTextView;
    private TextView moscowTextView;
    private TextView sochiTextView;
    private TextView parisTextView;
    private TextView milanTextView;
    private TextView omskTextView;
    private TextView novgorodTextView;
    private TextView krasnoyarskTextView;
    private TextView erevanTextView;
    private TextView dubaiTextView;
    private TextView pekinTextView;

    private CheckBox windCheckBox;
    private CheckBox pressureCheckBox;
    private CheckBox humidityCheckBox;
    private FloatingActionButton saveCitySettingsFAB;
    private TextInputEditText enterCityEditText;
    private static final String windDataKey = "windDataKey";
    private static final String pressureDataKey = "pressureDataKey";
    private static final String humidityDataKey = "humidityDataKey";
    private Publisher publisher;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_city_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setAction();
        if (!isTabletLandscape()){
            windCheckBox.setChecked(getCheckWind());
            humidityCheckBox.setChecked(getCheckHumidity());
            pressureCheckBox.setChecked(getCheckPressure());
        }
    }

    private void initViews(View view) {
        novosibirskTextView = view.findViewById(R.id.novosibirskTextView);
        moscowTextView= view.findViewById(R.id.moscowTextView);
        sochiTextView= view.findViewById(R.id.sochiTextView);
        parisTextView= view.findViewById(R.id.parisTextView);
        milanTextView= view.findViewById(R.id.milanTextView);
        omskTextView= view.findViewById(R.id.omskTextView);
        novgorodTextView= view.findViewById(R.id.novgorodTextView);
        krasnoyarskTextView= view.findViewById(R.id.krasnoyarskTextView);
        erevanTextView= view.findViewById(R.id.erevanTextView);
        dubaiTextView= view.findViewById(R.id.dubaiTextView);
        pekinTextView= view.findViewById(R.id.pekinTextView);

        windCheckBox = view.findViewById(R.id.windCheckBox);
        pressureCheckBox = view.findViewById(R.id.pressureCheckBox);
        humidityCheckBox = view.findViewById(R.id.humidityCheckBox);
        enterCityEditText = view.findViewById(R.id.enterCityEditText);
        saveCitySettingsFAB = view.findViewById(R.id.saveCitySettingsFAB);
    }

    private void setAction() {
        saveCitySettingsFAB.setOnClickListener(onSaveSelection);
        novosibirskTextView.setOnClickListener(v -> enterCityEditText.setText(novosibirskTextView.getText()));
        moscowTextView.setOnClickListener(v -> enterCityEditText.setText(moscowTextView.getText()));
        sochiTextView.setOnClickListener(v -> enterCityEditText.setText(sochiTextView.getText()));
        parisTextView.setOnClickListener(v -> enterCityEditText.setText(parisTextView.getText()));
        milanTextView.setOnClickListener(v -> enterCityEditText.setText(milanTextView.getText()));
        omskTextView.setOnClickListener(v -> enterCityEditText.setText(omskTextView.getText()));
        novgorodTextView.setOnClickListener(v -> enterCityEditText.setText(novgorodTextView.getText()));
        krasnoyarskTextView.setOnClickListener(v -> enterCityEditText.setText(krasnoyarskTextView.getText()));
        erevanTextView.setOnClickListener(v -> enterCityEditText.setText(erevanTextView.getText()));
        dubaiTextView.setOnClickListener(v -> enterCityEditText.setText(dubaiTextView.getText()));
        pekinTextView.setOnClickListener(v -> enterCityEditText.setText(pekinTextView.getText()));
    }



    View.OnClickListener onSaveSelection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cityName = Objects.requireNonNull(enterCityEditText.getText()).toString();
            if (cityName.equals("")) {
                Snackbar.make(requireActivity().findViewById(R.id.citySelectionLayout), R.string.enter_city,
                        Snackbar.LENGTH_LONG).show();
            } else {
                requireActivity().getSupportFragmentManager().popBackStack();

                if (MainActivity.isTabletLandscape) {
                    publisher.notify(cityName, windCheckBox.isChecked(), humidityCheckBox.isChecked(),
                            pressureCheckBox.isChecked());
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("returnData", getCoatContainer());
                    requireActivity().setResult(RESULT_OK, intent);
                    requireActivity().finish();
                }
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        boolean windBoolean = windCheckBox.isChecked();
        boolean pressureBoolean = pressureCheckBox.isChecked();
        boolean humidityBoolean = humidityCheckBox.isChecked();

        outState.putBoolean(windDataKey, windBoolean);
        outState.putBoolean(pressureDataKey, pressureBoolean);
        outState.putBoolean(humidityDataKey, humidityBoolean);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            boolean windBoolean = savedInstanceState.getBoolean(windDataKey);
            boolean pressureBoolean = savedInstanceState.getBoolean(pressureDataKey);
            boolean humidityBoolean = savedInstanceState.getBoolean(humidityDataKey);

            windCheckBox.setChecked(windBoolean);
            pressureCheckBox.setChecked(pressureBoolean);
            humidityCheckBox.setChecked(humidityBoolean);
        }
    }

    private boolean getCheckWind() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            return coatContainer.isShowWind;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean getCheckHumidity() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            return coatContainer.isShowHumidity;
        } catch (Exception e) {
            return true;
        }
    }

    private boolean getCheckPressure() {
        CoatContainer coatContainer = (CoatContainer) (Objects.requireNonNull(getArguments())
                .getSerializable("index"));
        try {
            return coatContainer.isShowPressure;
        } catch (Exception e) {
            return true;
        }
    }

    private CoatContainer getCoatContainer() {
        CoatContainer container = new CoatContainer();
        container.cityName = Objects.requireNonNull(enterCityEditText.getText()).toString();
        container.isShowWind = windCheckBox.isChecked();
        container.isShowHumidity = humidityCheckBox.isChecked();
        container.isShowPressure = pressureCheckBox.isChecked();
        return container;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (isTabletLandscape())
            publisher = ((PublisherGetter) requireContext()).getPublisher();
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
        publisher = null;
    }
}
