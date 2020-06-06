package com.zsl.upmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static  void main(String args[]) {
    }

    public static String DateToString(Date date, String dateFormat) {
        if(date == null){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static Date getCurrentStart(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime_2  = DateUtil.DateToString(new Date(),"yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(currentTime_2 + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurrentEnd(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime_2  = DateUtil.DateToString(new Date(),"yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(currentTime_2 + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
