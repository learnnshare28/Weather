package com.arjun.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arjun.weather.Model.FiveDaysWeather;
import com.arjun.weather.Model.ItemHourly;
import com.arjun.weather.RecyclerView.MainAdapter;
import com.arjun.weather.utils.Caps;
import com.arjun.weather.utils.DialogBox;
import com.arjun.weather.utils.Divide;
import com.arjun.weather.utils.FormatDate;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DialogBox.sendLocation, MainAdapter.setDataActivity {
    FusedLocationProviderClient providerClient;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    //Get all Views
    TextView date, weatherCondition, humidity, wind, bigTemperature, precipitation;
    ImageView icon;
    RecyclerView recyclerView;
    MainAdapter adapter;
    private int perm_id = 101;
    private boolean isTapped = false;
    private Menu menu;
    private Divide divide = new Divide();
    private FiveDaysWeather fiveDaysWeather = new FiveDaysWeather();
    private ArrayList<ItemHourly> itemHourlyArrayList = new ArrayList<>();
    private Weather weather;
    private String baseUrl = "https://api.openweathermap.org/data/2.5/";
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double lat = mLastLocation.getLatitude();
            double longs = mLastLocation.getLongitude();
            changeShimmerEffect(true);
            buildRetrofit(baseUrl, null, String.valueOf(lat), String.valueOf(longs));


        }
    };

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setAllViews();
        preferences = getSharedPreferences("Location", Activity.MODE_PRIVATE);
        editor = preferences.edit();
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        String loc = preferences.getString("location", null);
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        if (loc != null) buildRetrofit(baseUrl, loc, null, null);
        else buildRetrofit(baseUrl, "Newtown", null, null);


    }

    private void setAllViews() {
        weatherCondition = findViewById(R.id.weather);
        wind = findViewById(R.id.windData);
        humidity = findViewById(R.id.humData);
        precipitation = findViewById(R.id.pre);
        bigTemperature = findViewById(R.id.bigTemp);
        date = findViewById(R.id.weekday);
        icon = findViewById(R.id.imageIcon);
        setRecyclerView();


    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.rv);
        adapter = new MainAdapter(itemHourlyArrayList, this, this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_list, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_auto:
                if (!isTapped) {
                    isTapped = true;
                    if (checkPermission()) {
                        if (isLocationEnabled()) {
                            menu.getItem(0).getIcon().setTint(Color.parseColor("#0000FF"));
                            requestNewLocationData();

                        } else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    } else {
                        requestPermissions();
                    }


                } else {
                    isTapped = false;
                    menu.getItem(0).getIcon().setTint(Color.parseColor("#000000"));
                }

                break;
            case R.id.search_location:
                showDialogBox();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogBox() {
        DialogBox box = new DialogBox();
        box.show(getSupportFragmentManager(), "Search_Location");

    }


    private void getAllData(String lat, String lon, String userSearch, String apiKey) {
        Call<FiveDaysWeather> weatherCall;
        if (userSearch == null) {
            weatherCall = weather.getDataOnCoords(lat, lon, apiKey, "metric");
        } else {
            weatherCall = weather.getAllWeatherData(userSearch, apiKey, "metric");
        }

        weatherCall.enqueue(new Callback<FiveDaysWeather>() {
            @Override
            public void onResponse(Call<FiveDaysWeather> call, Response<FiveDaysWeather> response) {
                if (!response.isSuccessful()) {
                    buildNetworkError(response.code());
                    return;
                }
                assert response.body() != null;
                fiveDaysWeather = response.body();
                setDataFromAPI();
            }

            @Override
            public void onFailure(Call<FiveDaysWeather> call, Throwable t) {
                changeShimmerEffect(false);
                buildSnackBar(lat, lon, null);
            }
        });

    }


    void changeShimmerEffect(boolean todo) {
        adapter.isShimmer = todo;
        adapter.notifyDataSetChanged();
    }

    private void buildNetworkError(int error) {
        changeShimmerEffect(false);
        switch (error) {
            case 404:
                Snackbar.make(weatherCondition, "No Such Location Found", Snackbar.LENGTH_LONG).show();
                break;
            case 408:
                Snackbar.make(weatherCondition, "Bad Internet Connection", Snackbar.LENGTH_LONG).show();
                break;
            case 500:
                Snackbar.make(weatherCondition, "Internal Server Error", Snackbar.LENGTH_LONG).show();
        }
    }

    private void buildSnackBar(String lat, String lon, String userData) {
        Snackbar.make(weatherCondition, "Internet Connection Failed", Snackbar.LENGTH_INDEFINITE)
                .setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (userData == null) {
                            buildRetrofit(baseUrl, null, String.valueOf(lat), String.valueOf(lon));
                        } else {
                            buildRetrofit(baseUrl, userData, null, null);
                        }
                    }
                })
                .show();

    }

    @SuppressLint("SetTextI18n")
    private void setDataFromAPI() {
        changeShimmerEffect(false);
        divide.convertData(fiveDaysWeather);
        menu.getItem(0).getIcon().setTint(Color.parseColor("#000000"));
        Objects.requireNonNull(getSupportActionBar()).setTitle(fiveDaysWeather.getCity().getName() + ", " + fiveDaysWeather.getCity().getCountry());
        weatherCondition.setText(new Caps().capitalizeWord(fiveDaysWeather.getList().get(0).getWeather().get(0).getDescription()));
        wind.setText((fiveDaysWeather.getList().get(0).getWind().getSpeed()) + " km/hr");
        humidity.setText((fiveDaysWeather.getList().get(0).getMain().getHumidity()) + "mm");
        precipitation.setText((fiveDaysWeather.getList().get(0).getMain().getTemp() + "°C"));
        bigTemperature.setText(fiveDaysWeather.getList().get(0).getMain().getTemp() + "°C");
        Glide
                .with(MainActivity.this)
                .load("https://openweathermap.org/img/wn/"
                        + fiveDaysWeather.getList().get(0).getWeather().get(0).getIcon()
                        + "@2x.png").into(icon);
        FormatDate formatDate = new FormatDate();
        date.setText(formatDate.convertDate(fiveDaysWeather.getList().get(0).getDtTxt()));
        bottom_data();
    }

    private void bottom_data() {
        if(itemHourlyArrayList.size()!=0){
            itemHourlyArrayList.clear();
        }
        itemHourlyArrayList.add(divide.getDayOne().get(0));
        itemHourlyArrayList.add(divide.getDayTwo().get(3));
        itemHourlyArrayList.add(divide.getDayThree().get(3));
        itemHourlyArrayList.add(divide.getDayFOur().get(3));
        itemHourlyArrayList.add(divide.getDayFive().get(3));
    }

    private void buildRetrofit(String baseUrl, String userSearch, String lat, String lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weather = retrofit.create(Weather.class);
        //API key will go down here
        String appid = new API().sendAPIKEY();
        if (lat == null && lon == null) {
            getAllData(null, null, userSearch, appid);
        } else {
            getAllData(String.valueOf(lat), String.valueOf(lon), null, appid);
        }
    }


    @Override
    public void sendUserLocation(String location, Boolean toSave) {
        if (toSave) {
            //Store user Preference here
            editor.putString("location", location);
            editor.apply();
        }
        changeShimmerEffect(true);
        buildRetrofit(baseUrl, location, null, null);

    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, perm_id);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == perm_id) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocationData();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        // setting LocationRequest on FusedLocationClient
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        providerClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void setPosition(int position) {
        ArrayList<ItemHourly> itemHourlies = new ArrayList<>();
        if(itemHourlies.size()!=0){
            itemHourlies.clear();
            Log.e("itemsHourlies", "itemsHourlies size: "+itemHourlies.size());
        }
        if (position == 0) {
            itemHourlies = divide.getDayOne();
        } else if (position == 1) {
            itemHourlies = divide.getDayTwo();
        } else if (position == 2) {
            itemHourlies = divide.getDayThree();
        } else if (position == 3) {
            itemHourlies = divide.getDayFOur();
        } else {
            itemHourlies = divide.getDayFive();
        }
        Intent intent = new Intent(this, ItemsHourly.class);
        intent.putExtra("private_list", new Gson().toJson(itemHourlies));
        startActivity(intent);
    }

}