package com.example.cleansafi.mpesa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtil {
    public static String getTimestamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        return timeStamp;
    }
}
