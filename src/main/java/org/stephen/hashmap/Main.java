package org.stephen.hashmap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class Main {

    public static void main (String[] args) throws ExecutionException, InvocationTargetException, IllegalAccessException {
        final GuavaCache cache = new GuavaCache ();
        List<String> propertyList = getPropertyList ();

        long startTime, endTime;
        for (int i = 0; i < 1001; ++i) {
            for (String property : propertyList) {
                startTime = System.nanoTime ();
                cache.get (property);
                endTime = System.nanoTime () - startTime;

                if (i % 10 == 0) {
                    final String times = String.format ("%01$,.4f ms, %2$,.6f sec", (endTime / 1000000.0), endTime / 1000000000.0);
                    System.out.println (String.format ("[%03d] Time to retrieve property <%s>%-" + (50 - property.length ()) + "s: %010d ns (%s)", i, property, " ", endTime, times));
                }
            }
        }
        System.out.println ("Finished!");
    }

    private static List<String> getPropertyList () {
        List<String> propertyList = new ArrayList<> (3);
        propertyList.add (String.format ("%s.stringValue_01", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.stringValue_02", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.stringValue_03", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.stringValue_04", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.doubleValue_01", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.doubleValue_02", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.doubleValue_03", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.doubleValue_04", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.longValue_01", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.longValue_02", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.longValue_03", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.longValue_04", CacheObject.class.getName ()));
        return propertyList;
    }
}

