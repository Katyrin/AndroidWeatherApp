package com.katyrin.weatherapp.fragments;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

public class SaveCastFragment implements Serializable {
    public Fragment fragment;

    private static SaveCastFragment instance;

    private SaveCastFragment(){}

    public static SaveCastFragment getInstance(){
        if (instance == null)
            instance = new SaveCastFragment();
        return instance;
    }
}
