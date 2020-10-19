package com.arjun.weather.utils;

import android.annotation.SuppressLint;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatDate {
     public String convertDate(String date)  {
         @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM dd");
         @SuppressLint("SimpleDateFormat") Date date1= null;
         try {
             date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0,10));
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return format.format(date1);
     }
     @SuppressLint("SimpleDateFormat")
     public String onlyDay(String date){
         @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEEE");
         @SuppressLint("SimpleDateFormat") Date date1= null;
         try {
             date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0,10));
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return format.format(date1);
     }
     @SuppressLint("SimpleDateFormat")
     public String mon_day(String date){
         @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("MMM dd hh:mm a");
         @SuppressLint("SimpleDateFormat") Date date1= null;
         try {
             date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return format.format(date1);
     }
    @SuppressLint("SimpleDateFormat")
    public String time(String date) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("hh");
        @SuppressLint("SimpleDateFormat") Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date1 != null;
        return format.format(date1);
    }

}
