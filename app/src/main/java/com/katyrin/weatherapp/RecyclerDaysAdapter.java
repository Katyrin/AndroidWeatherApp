package com.katyrin.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.katyrin.weatherapp.fragments.MainWeatherFragment;

import java.util.ArrayList;

public class RecyclerDaysAdapter extends RecyclerView.Adapter<RecyclerDaysAdapter.ViewHolder> {
    private final ArrayList<DataRVClass> dataDays;
    private final IRVDaysOnItemClick onItemClickCallBack;
    private final Context context;

    public RecyclerDaysAdapter(ArrayList<DataRVClass> dataDays,
                               IRVDaysOnItemClick onItemClickCallBack, Context context) {
        this.dataDays = dataDays;
        this.onItemClickCallBack = onItemClickCallBack;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_day_rv_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dayName.setText(dataDays.get(position).dayName);
        holder.dayTemperature.setText(dataDays.get(position).dayTemperature);

        int icon = MainWeatherFragment.icons.get(dataDays.get(position).icon);

        holder.dayImageView.setImageDrawable(ContextCompat.getDrawable(
                context, icon));
        setOnClickForItem(holder, dataDays.get(position).dayName,
                dataDays.get(position).dayTemperature, icon,
                dataDays.get(position).windSpeed, dataDays.get(position).humidity,
                dataDays.get(position).pressure);
    }

    @Override
    public int getItemCount() {
        return dataDays == null ? 0 : dataDays.size();
    }

    private void setOnClickForItem(@NonNull ViewHolder holder, String dayName, String dayTemperature,
                                   int drawableId, String wind, String humidity, String pressure) {
        holder.dayItemLayout.setOnClickListener(v -> {
            if (onItemClickCallBack != null) {
                onItemClickCallBack.onItemClicked(dayName, dayTemperature, drawableId,
                        wind, humidity, pressure);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout dayItemLayout;
        TextView dayName;
        TextView dayTemperature;
        ImageView dayImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dayItemLayout = itemView.findViewById(R.id.dayItemLayout);
            dayName = itemView.findViewById(R.id.dayName);
            dayTemperature = itemView.findViewById(R.id.dayTemperature);
            dayImageView = itemView.findViewById(R.id.dayImageView);
        }
    }
}
