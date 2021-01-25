package com.katyrin.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerCitiesAdapter extends RecyclerView.Adapter<RecyclerCitiesAdapter.ViewHolder> {
    private ArrayList<String> citiesData;
    private IRVCitiesOnItemClick onItemClickCallBack;

    public RecyclerCitiesAdapter(ArrayList<String> citiesData, IRVCitiesOnItemClick onItemClickCallBack) {
        this.citiesData = citiesData;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_city_rv_layout, parent, false);
        return new RecyclerCitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setItemText(holder, citiesData.get(position));
        setOnClickForItem(holder, citiesData.get(position));
    }

    private void setItemText(@NonNull ViewHolder holder, String cityName) {
        holder.cityNameTextView.setText(cityName);
    }

    private void setOnClickForItem(@NonNull ViewHolder holder, String cityName) {
        holder.cityItemLayout.setOnClickListener(v -> {
            if (onItemClickCallBack != null)
                onItemClickCallBack.onItemCityClicked(cityName);
        });
    }

    @Override
    public int getItemCount() {
        return citiesData == null ? 0 : citiesData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTextView;
        FrameLayout cityItemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityItemLayout = itemView.findViewById(R.id.cityItemLayout);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
        }
    }
}
