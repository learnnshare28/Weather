package com.arjun.weather;

import com.arjun.weather.Model.FiveDaysWeather;
import com.arjun.weather.Model.OneDay;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Weather {

    @GET("forecast")
    Call<FiveDaysWeather> getAllWeatherData(@Query("q") String query,
                                            @Query("appid")String apiKey,
                                            @Query("units")String s);

    @GET("forecast")
    Call<FiveDaysWeather> getDataOnCoords(@Query("lat") String lat,
                                            @Query("lon")String lon,
                                            @Query("appid")String apiKey,
                                            @Query("units")String s);

    @GET("forecast")
    Call<OneDay> getOneDay(@Query("q") String query,
                                   @Query("appid")String apiKey,
                                   @Query("units")String s);



}
