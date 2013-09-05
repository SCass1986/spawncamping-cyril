package org.stephen.hashmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static final String LOG_STRING = "[%s] %-10s: ";
    private static final String INFO       = "INFO";
    private static final String DEBUG      = "DEBUG";
    private static final String VERBOSE    = "VERBOSE";

    private static final Calendar calendar = Calendar.getInstance ();

    public static void info (final String message, final Object... args) {
        if (Main.LOGGING_INFO_ON) {
            System.out.println (String.format ("%s%s", String.format (LOG_STRING, getTimeStamp (), INFO), String.format (message, args)));
        }
    }

    public static void verbose (final String message, final Object... args) {
        if (Main.LOGGING_VERBOSE_ON) {
            System.out.println (String.format ("%s%s", String.format (LOG_STRING, getTimeStamp (), INFO), String.format (message, args)));
        }
    }

    public static void debug (final String message, final Object... args) {
        if (Main.LOGGING_DEBUG_ON) {
            System.out.println (String.format ("%s%s", String.format (LOG_STRING, getTimeStamp (), DEBUG), String.format (message, args)));
        }
    }

    private static String getTimeStamp () {
        return new SimpleDateFormat ("dd/mm/yyyy hh:MM:ss").format (calendar.getTime ());
    }

}
