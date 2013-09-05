package org.stephen.hashmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Logger {
    private static final String LOG_STRING = "[%s] %-10s: ";

    public static void info (final String message, final Object... args) {

        System.out.println (String.format ("%s%s", String.format (LOG_STRING, getTimeStamp (), "INFO"), String.format (message, args)));
    }

    private static String getTimeStamp () {
        final Date time = Calendar.getInstance ().getTime ();
        return new SimpleDateFormat ("dd/mm/yyyy hh:MM:ss").format (time);
    }

}
