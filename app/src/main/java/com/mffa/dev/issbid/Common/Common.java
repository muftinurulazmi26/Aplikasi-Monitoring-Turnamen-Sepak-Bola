package com.mffa.dev.issbid.Common;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static final String APP_ID ="c2541b6b7c32e00a0eac21484e8dfefc";
    public static final String BASE_URL ="http://api.openweathermap.org/data/2.5/";
    public static Location location = null;
    public static double latitude;
    public static double longitude;

    public static String convertDate (long dt){
        Date date = new Date(dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEE MM yyyy");
        String fm = simpleDateFormat.format(date);
        return fm;
    }

    public static String convertHour (long sunrise){
        Date date = new Date(sunrise*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String fm = simpleDateFormat.format(date);
        return fm;
    }
}
