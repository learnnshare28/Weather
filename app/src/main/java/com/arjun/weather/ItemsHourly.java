package com.arjun.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arjun.weather.Model.ItemHourly;
import com.arjun.weather.RecyclerView.Adapter;
import com.arjun.weather.utils.FormatDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ItemsHourly extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<ItemHourly> listPrivate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hourly);
        listPrivate = new ArrayList<>();
        Type type = new TypeToken<List<ItemHourly>>() {
        }.getType();
        listPrivate = new Gson().fromJson(getIntent().getStringExtra("private_list"), type);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        getSupportActionBar().setTitle(new FormatDate().onlyDay(listPrivate.get(0).getDtTxt()) + "'s Weather");

        setRecyclerView();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            adapter.isShimmer = false;
            adapter.notifyDataSetChanged();
        }, 0, 3000);
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.hourly_main);
        adapter = new Adapter(listPrivate, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
