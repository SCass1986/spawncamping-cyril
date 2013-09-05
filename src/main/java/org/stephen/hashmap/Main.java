package org.stephen.hashmap;

import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.ClassPropertyUtil;
import org.stephen.hashmap.caches.guava.GuavaCache;
import org.stephen.hashmap.caches.lru.LeastRecentlyUsedCache;
import org.stephen.hashmap.caches.lru.eviction.EvictBySize;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.config.AppConfig;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public final class Main {
    public static final boolean LOGGING_INFO_ON    = true;
    public static final boolean LOGGING_DEBUG_ON   = false;
    public static final boolean LOGGING_VERBOSE_ON = false;

    public static void main (String[] args) throws ExecutionException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Logger.info ("Starting!");
        int testIterations = AppConfig.INSTANCE.getInt ("guava.cache.test.iterations", 1001);
        Logger.info ("Testing Guava ClassPropertyCache");
        testCache (getGuavaCache (), testIterations);

        Logger.info ("Testing LinkedHashMap ClassPropertyCache");
        testIterations = AppConfig.INSTANCE.getInt ("lru.cache.test.iterations", 1001);
        testCache (getLinkedHashMapCache (), testIterations);

        Logger.info ("Testing ClassUtilCache with LinkedHashMap");
        testCache (getPropertyCache (new LeastRecentlyUsedCache.Builder ()
                                             .withDefaults ()
                                             .build ()),
                   1001);
        Logger.info ("Testing ClassUtilCache with GuavaCache");
        testCache (getPropertyCache (new GuavaCache.Builder ()
                                             .withDefaults ()
                                             .build ()),
                   1001);
        Logger.info ("Finished!");
    }

    private static void testCache (final ClassPropertyCache<PropertyKey, PropertyHolder> cache, final int testIterations) throws ClassNotFoundException {
        List<String> propertyList = getPropertyList ();
        final String cacheClass = cache.getClass ().getSimpleName ();
        long iterationStartTime, iterationEndTime, startTime, endTime;

        final CacheObject cacheObject = new CacheObject ("stringValue", 134678L, 10.99);
        startTime = System.nanoTime ();
        for (int i = 0; i < testIterations; ++i) {
            for (final String property : propertyList) {
                iterationStartTime = System.nanoTime ();
                cache.get (property).getValue (cacheObject);
                iterationEndTime = System.nanoTime ();

                if (LOGGING_VERBOSE_ON) {
                    if (i % 50 == 0) {
                        final long timeForIteration = iterationEndTime - iterationStartTime;
                        final String times = getTimeString (timeForIteration);
                        Logger.verbose ("[%05d] [%s] Time to retrieve property <%s>%-" + (50 - property.length ()) + "s: %010d ns (%s)",
                                        i, cacheClass, property, " ", iterationEndTime - iterationStartTime, times);
                    }
                }
            }
        }
        endTime = System.nanoTime () - startTime;
        Logger.info ("[%s] Total Time : %010d ns (%s)", cacheClass, endTime, getTimeString (endTime));
    }

    private static ClassPropertyUtil getPropertyCache (final ClassPropertyCache<PropertyKey, PropertyHolder> cache) {
        return new ClassPropertyUtil (cache);
    }

    private static ClassPropertyCache getLinkedHashMapCache () {
        return new LeastRecentlyUsedCache.Builder ()
                .withInitialCapacity (32)
                .withLoadFactor (0.75f)
                .withAccessOrder (true)
                .withEvictionStrategy (new EvictBySize (32))
                .build ();
    }

    private static ClassPropertyCache getGuavaCache () {
        final int maxSize = AppConfig.INSTANCE.getInt ("guava.cache.max_size", 100);
        final int concurrencyLevel = AppConfig.INSTANCE.getInt ("guava.cache.concurrency_level", 1);
        final int expireTime = AppConfig.INSTANCE.getInt ("guava.cache.expire_after_access_time", 100);
        final TimeUnit timeUnit = TimeUnit.valueOf (AppConfig.INSTANCE.getString ("guava.cache.expire_after_access_time_unit", "SECONDS"));

        System.out.println (String.format ("+------------------------------------+\n" +
                                           "| Guava Cache Parameters:\n" +
                                           "|  Maximum Size             : %s\n" +
                                           "|  Concurrency Level        : %s\n" +
                                           "|  Expire After Access Time : %s %s\n" +
                                           "+------------------------------------+",
                                           maxSize, concurrencyLevel, expireTime, timeUnit));
        return new GuavaCache.Builder ()
                .withMaximumSize (maxSize)
                .withConcurrencyLevel (concurrencyLevel)
                .withExpireAfterAccessTime (100)
                .withExpireAfterAccessTimeUnit (TimeUnit.SECONDS)
                .build ();
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

