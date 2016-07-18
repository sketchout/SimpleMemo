package com.zeiyu.simplememo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ZeiYu on 7/7/2016.
 */
public class StrUtil {


    public static String timestampToString(Long timeStamp){
        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            //Date dt = new Date( timeTextView * 1000 );
            // return sdf.format(dt);

            //Calendar cal = Calendar.getInstance();
            //cal.setTimeInMillis(timeTextView);
            Date netDate = new Date(timeStamp);
            return sdf.format(netDate);

        } catch (Exception ignored) {
            return "??:??:??";
        }
    }

    public static String timestampToDate(Long timeStamp){
        try{
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            //Date dt = new Date( timeTextView * 1000 );
            // return sdf.format(dt);

            //Calendar cal = Calendar.getInstance();
            //cal.setTimeInMillis(timeTextView);
            Date netDate = new Date(timeStamp);
            return sdf.format(netDate);

        } catch (Exception ignored) {
            return "??:??:??";
        }
    }

    public static String timestampToTime(Long timeStamp){
        try{
            DateFormat sdf = new SimpleDateFormat("HH:mm");
            //Date dt = new Date( timeTextView * 1000 );
            // return sdf.format(dt);

            //Calendar cal = Calendar.getInstance();
            //cal.setTimeInMillis(timeTextView);
            Date netDate = new Date(timeStamp);
            return sdf.format(netDate);

        } catch (Exception ignored) {
            return "??:??:??";
        }
    }
}
