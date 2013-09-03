package org.stephen.hashmap;

import org.stephen.hashmap.caches.AbstractPropertyCache;
import org.stephen.hashmap.caches.guava.GuavaCache;
import org.stephen.hashmap.caches.lru.LinkedHashMapCache;
import org.stephen.hashmap.caches.lru.eviction.EvictBySize;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class Main {

    public static void main (String[] args) throws ExecutionException, InvocationTargetException, IllegalAccessException {
        System.out.println ("Testing Guava Cache");
        testCache (getGuavaCache ());
        System.out.println ("Testing LinkedHashMap Cache");
        testCache (getLinkedHashMapCache ());
        System.out.println ("Finished!");
    }

    private static GuavaCache getGuavaCache () {
        return new GuavaCache ();
    }

    private static LinkedHashMapCache getLinkedHashMapCache () {
        return new LinkedHashMapCache.Builder ()
                .withInitialCapacity (16)
                .withLoadFactor (0.75f)
                .withAccessOrder (true)
                .withEvictionStrategy (new EvictBySize (5))
                .build ();
    }

    private static void testCache (final AbstractPropertyCache<String> cache) {
        List<String> propertyList = getPropertyList ();
        final String cacheClass = cache.getClass ().getSimpleName ();
        long iterationStartTime, iterationEndTime, startTime, endTime;

        startTime = System.nanoTime ();
        for (int i = 0; i < 1001; ++i) {
            for (final String property : propertyList) {
                iterationStartTime = System.nanoTime ();
                cache.get (property);
                iterationEndTime = System.nanoTime () - iterationStartTime;

                if (i % 10 == 0) {
                    final String times = getTimeString (iterationEndTime);
                    System.out.println (String.format ("[%03d] [%s] Time to retrieve property <%s>%-" + (50 - property.length ()) + "s: %010d ns (%s)", i, cacheClass, property, " ", iterationEndTime, times));
                }
            }
        }
        endTime = System.nanoTime () - startTime;
        System.out.println (String.format ("[%s] Total Time : %010d ns (%s)", cacheClass, endTime, getTimeString (endTime)));
    }

    private static String getTimeString (final long time) {
        return String.format ("%01$,.4f ms, %2$,.6f sec", (time / 1000000.0), time / 1000000000.0);
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

