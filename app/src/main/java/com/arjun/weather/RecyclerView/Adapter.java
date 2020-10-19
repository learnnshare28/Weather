package com.arjun.weather.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arjun.weather.Model.ItemHourly;
import com.arjun.weather.R;
import com.arjun.weather.utils.FormatDate;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public boolean isShimmer = true;
    int shimmerNumber = 8;

    ArrayList<ItemHourly> hourlyArrayList = new ArrayList<>();
    Context context;

    public Adapter(ArrayList<ItemHourly> itemHourlyList, Context context) {
        if (hourlyArrayList.size() != 0) {
            hourlyArrayList.clear();
            hourlyArrayList.addAll(itemHourlyList);
        } else {
            this.hourlyArrayList = itemHourlyList;
        }
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlehourly,
                parent,
                false);
        return new ViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShimmer) {
            holder.frameLayout.startShimmerAnimation();
        } else {
            holder.frameLayout.stopShimmerAnimation();
            Glide.with(context)
                    .load("https://openweathermap.org/img/wn/" + hourlyArrayList.get(position).getWeather().get(0).getIcon() + "@2x.png")
                    .into(holder.icon1);

            holder.tempmin.setText(hourlyArrayList.get(position).getMain().getTempMin() + "°C");
            holder.tempmax.setText(hourlyArrayList.get(position).getMain().getTempMax() + "°C");
            holder.day.setText(new FormatDate().onlyDay(hourlyArrayList.get(position).getDtTxt()));
            holder.frameLayout.setBackground(null);
            holder.date.setText(new FormatDate().mon_day(hourlyArrayList.get(position).getDtTxt()));
        }

    }

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber : hourlyArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon1;
        ShimmerFrameLayout frameLayout;
        TextView tempmax, tempmin, date, day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon1 = itemView.findViewById(R.id.weather_image_view);
            tempmax = itemView.findViewById(R.id.max_temp_text_view);
            frameLayout = itemView.findViewById(R.id.smallShimmer);
            date = itemView.findViewById(R.id.date_text_view);
            day = itemView.findViewById(R.id.day_name_text_view);
            tempmin = itemView.findViewById(R.id.min_temp_text_view);
        }
    }
}
