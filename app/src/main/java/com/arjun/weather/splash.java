package com.arjun.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class splash extends AppCompatActivity {
LottieAnimationView animationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        animationView = findViewById(R.id.loading);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            animationView.pauseAnimation();
            Intent intent = new Intent(splash.this,MainActivity.class);
            startActivity(intent);
            finish();
        }, 0, 4000);

    }
}

