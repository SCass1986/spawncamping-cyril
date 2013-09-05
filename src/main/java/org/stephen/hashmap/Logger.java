package org.stephen.hashmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static final String   LOG_STRING = "[%s] %-10s: ";
    private static final Calendar calendar   = Calendar.getInstance ();

    public static void info (final String message, final Object... args) {
        if (Main.LOGGING_ON) {
            System.out.println (String.format ("%s%s", String.format (LOG_STRING, getTimeStamp (), "INFO"), String.format (message, args)));
        }
    }

    private static String getTimeStamp () {
        return new SimpleDateFormat ("dd/mm/yyyy hh:MM:ss").format (calendar.getTime ());
    }

}
