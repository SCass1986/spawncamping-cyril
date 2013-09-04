package org.stephen.hashmap;

import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.guava.GuavaCache;
import org.stephen.hashmap.caches.lru.LinkedHashMapCache;
import org.stephen.hashmap.caches.lru.eviction.EvictBySize;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.config.ApplicationConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.config.ConfigProperties.GUAVA_CACHE_MAXSIZE;
import static org.stephen.hashmap.config.ConfigProperties.GUAVA_CONCURRENCY_LEVEL;

public final class Main {

    public static void main (String[] args) throws ExecutionException, InvocationTargetException, IllegalAccessException {
        System.out.println ("Starting!");
        System.out.println ("Testing Guava ClassPropertyCache");
        testCache (getGuavaCache ());
        System.out.println ("Testing LinkedHashMap ClassPropertyCache");
        testCache (getLinkedHashMapCache ());
        System.out.println ("Finished!");
    }

    private static GuavaCache getGuavaCache () {
        final int maxSize = ApplicationConfig.INSTANCE.getInteger (GUAVA_CACHE_MAXSIZE.getKey (),
                                                                   GUAVA_CACHE_MAXSIZE.getDefault ());
        final int concurrencyLevel = ApplicationConfig.INSTANCE.getInteger (GUAVA_CONCURRENCY_LEVEL.getKey (),
                                                                            GUAVA_CONCURRENCY_LEVEL.getDefault ());
        System.out.println (String.format ("+------------------------------------+\n" +
                                           "| Guava Cache Parameters:\n" +
                                           "|  Maximum Size       : %s\n" +
                                           "|  Concurrency Level  : %s\n" +
                                           "+------------------------------------+",
                                           maxSize, concurrencyLevel));
        return new GuavaCache.Builder (maxSize)
                .withConcurrencyLevel (concurrencyLevel)
                .withExpireAfterAccessTime (100)
                .withExpireAfterAccessTimeUnit (TimeUnit.SECONDS)
                .build ();
    }

    private static LinkedHashMapCache getLinkedHashMapCache () {
        return new LinkedHashMapCache.Builder ()
                .withInitialCapacity (16)
                .withLoadFactor (0.75f)
                .withAccessOrder (true)
                .withEvictionStrategy (new EvictBySize (5))
                .build ();
    }

    private static void testCache (final ClassPropertyCache<String, PropertyHolder> cache) {
        List<String> propertyList = getPropertyList ();
        final String cacheClass = cache.getClass ().getSimpleName ();
        long iterationStartTime, iterationEndTime, startTime, endTime;

        final CacheObject cacheObject = new CacheObject ("stringValue", 134678L, 10.99);
        startTime = System.nanoTime ();
        for (int i = 0; i < 0; ++i) {
            for (final String property : propertyList) {
                iterationStartTime = System.nanoTime ();
                cache.get (property).getValue (cacheObject);
                iterationEndTime = System.nanoTime () - iterationStartTime;

                if (i % 50 == 0) {
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

