package com.arjun.weather.Model;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FiveDaysWeather {
    @SerializedName("city")
    private City city;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private double message;

    @SerializedName("list")
    private ArrayList<ItemHourly> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public ArrayList<ItemHourly> getList() {
        return list;
    }

    public void setList(ArrayList<ItemHourly> list) {
        this.list = list;
    }
}
