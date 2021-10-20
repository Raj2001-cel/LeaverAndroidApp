package com.example.leaverandroidapppoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class MyUtil {

    @SuppressLint("SimpleDateFormat")
    public static String formatDate(Context context, Date date){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String dateFormat = prefs.getString("dateFormat","dd/MM/yyyy");
        //Needs to be removed once default changes are done in backend as MM -> Months & mm -> minutes
        if(dateFormat.equals("dd/mm/yyyy"))
            dateFormat = "dd/MM/yyyy";
        //upto here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatTime(Context context, Date date){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean timeFormat = prefs.getBoolean("timeFormat",true);
        String format;
        if(timeFormat)
            format = "HH.mm aa";
        else
            format = "hh.mm aa";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    public static String getProperty(String key, Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("application.properties");
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
