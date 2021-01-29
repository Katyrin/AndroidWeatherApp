package com.katyrin.weatherapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.katyrin.weatherapp.R;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    public interface MyBottomSheetDialogFragmentListener {
        void onDialogSelectCity(String cityName);
    }

    private TextInputEditText enterCityEditText;
    private MyBottomSheetDialogFragmentListener listener;

    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }

    public void setMyBottomSheetDialogFragmentListener(MyBottomSheetDialogFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        setCancelable(false);
        enterCityEditText = view.findViewById(R.id.enterCityEditText);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        view.findViewById(R.id.enterDialogButton).setOnClickListener(view1 -> {
            dismiss();
            String cityName = enterCityEditText.getText().toString();
            if (listener != null)
                if (cityName.equals(""))
                    Snackbar.make(requireActivity().findViewById(R.id.mainLayout), R.string.enter_city,
                            Snackbar.LENGTH_LONG).setAnchorView(R.id.nav_view).show();
                else
                    listener.onDialogSelectCity(cityName);
        });
        view.findViewById(R.id.cancelDialogButton).setOnClickListener(view1 -> {
            dismiss();
        });
        return view;
    }
}
