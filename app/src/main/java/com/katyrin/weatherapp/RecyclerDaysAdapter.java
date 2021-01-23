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

import java.util.ArrayList;

public class RecyclerDaysAdapter extends RecyclerView.Adapter<RecyclerDaysAdapter.ViewHolder> {
    private ArrayList<DataRVClass> dataDays;
    private IRVDaysOnItemClick onItemClickCallBack;
    private Context context;

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
        holder.dayImageView.setImageDrawable(ContextCompat.getDrawable(
                context, dataDays.get(position).drawableId));
        setOnClickForItem(holder, dataDays.get(position).dayName,
                dataDays.get(position).dayTemperature, dataDays.get(position).drawableId);
    }

    @Override
    public int getItemCount() {
        return dataDays == null ? 0 : dataDays.size();
    }

    private void setOnClickForItem(@NonNull ViewHolder holder, String dayName, String dayTemperature,
                                   int drawableId) {
        holder.dayItemLayout.setOnClickListener(v -> {
            if (onItemClickCallBack != null) {
                onItemClickCallBack.onItemClicked(dayName, dayTemperature, drawableId);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
