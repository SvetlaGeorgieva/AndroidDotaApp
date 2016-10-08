package com.fivepins.matchtracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by e30 on 9/12/2016.
 * Global utilities class
 */
public class Utils {

    public static String getDateToday() {
        return getDateDaysBack(0);
    }

    public static String getDateDaysBack(int daysBack) {
        // This should be used when we use the user's local locale
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysBack*(-1));
        return dateFormat.format(calendar.getTime());
    }

}
