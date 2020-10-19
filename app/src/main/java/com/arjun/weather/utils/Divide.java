package com.arjun.weather.utils;

import android.annotation.SuppressLint;

import com.arjun.weather.Model.FiveDaysWeather;
import com.arjun.weather.Model.ItemHourly;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Divide {
    private ArrayList<ItemHourly> dayOne = new ArrayList<>();
    private ArrayList<ItemHourly> dayTwo = new ArrayList<>();
    private ArrayList<ItemHourly> dayThree = new ArrayList<>();
    private ArrayList<ItemHourly> dayFour = new ArrayList<>();
    private ArrayList<ItemHourly> dayFive = new ArrayList<>();


    public void convertData(FiveDaysWeather daysWeather) {
        if (dayOne.size()!= 0) {
            dayOne.clear();
            dayTwo.clear();
            dayThree.clear();
            dayFour.clear();
            dayFive.clear();
        }
        int day = getDay(daysWeather.getList().get(0).getDtTxt());
        for (ItemHourly hourly : daysWeather.getList()) {
            if (getDay(hourly.getDtTxt()) == day) {
                dayOne.add(hourly);
            } else if (getDay(hourly.getDtTxt()) == day + 1) {
                dayTwo.add(hourly);
            } else if (getDay(hourly.getDtTxt()) == day + 2) {
                dayThree.add(hourly);
            } else if (getDay(hourly.getDtTxt()) == day + 3) {
                dayFour.add(hourly);
            } else if (getDay(hourly.getDtTxt()) == day + 4) {
                dayFive.add(hourly);
            }
        }

    }

    public ArrayList<ItemHourly> getDayOne() {
        return dayOne;
    }

    public ArrayList<ItemHourly> getDayTwo() {
        return dayTwo;
    }

    public ArrayList<ItemHourly> getDayThree() {
        return dayThree;
    }

    public ArrayList<ItemHourly> getDayFOur() {
        return dayFour;
    }

    public ArrayList<ItemHourly> getDayFive() {
        return dayFive;
    }

    @SuppressLint("SimpleDateFormat")
    private int getDay(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0, 10));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date1 != null;
        return Integer.parseInt(format.format(date1));
    }
}
