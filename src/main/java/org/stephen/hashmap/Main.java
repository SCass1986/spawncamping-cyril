package org.stephen.hashmap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: stephen
 * Date: 01/09/13
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main (String[] args) throws ExecutionException {
        final GuavaCache cache = new GuavaCache ();
        List<String> propertyList = getPropertyList ();
        for (int i = 0; i < 101; ++i) {
            for (String property : propertyList) {
                final long startTime = System.nanoTime ();
                cache.get (property);
                if (i % 10 == 0) {
                    final long endTime = System.nanoTime () - startTime;
                    final String times = String.format ("%01$,.4f ms, %2$,.6f sec", (endTime / 1000000.0), endTime / 1000000000.0);
                    System.out.println (String.format ("[%03d] Time to retrieve property <%s>%-" + (50 - property.length ()) + "s: %010d ns (%s)", i, property, " ", endTime, times));
                }
            }
        }
        System.out.println ("Finished!");
    }

    private static List<String> getPropertyList () {
        List<String> propertyList = new ArrayList<> (3);
        propertyList.add (String.format ("%s.stringValue", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.doubleValue", CacheObject.class.getName ()));
        propertyList.add (String.format ("%s.longValue", CacheObject.class.getName ()));
        return propertyList;
    }

//    public static void main (String[] args) {
//        final Cache cache = new Cache ();
//        for (int i = 0; i < 2; ++i) {
//            final String className = Cache.createRandomString (25);
//            final String method = Cache.createRandomString (10);
//            final String property = Cache.createRandomString (75);
//            cache.get (className, method, property);
//            cache.get (className, method, property);
//            System.out.println ("+----------------------------------------------------------------+");
//        }
//
////        testStringPoolGarbageCollection ();
//        testLongLoop ();
//    }
//
//    /**
//     * Use this method to see where interned strings are stored
//     * and how many of them can you fit for the given heap size.
//     */
//    private static void testLongLoop () {
//        test (1000 * 1000);
//        //uncomment the following line to see the hand-written cache performance
//        testManual( 1000 * 1000 );
//    }
//
//    /**
//     * Use this method to check that not used interned strings are garbage collected.
//     */
//    private static void testStringPoolGarbageCollection () {
//        //first method call - use it as a reference
//        test (1000 * 1000);
//        //we are going to clean the cache here.
//        System.gc ();
//        //check the memory consumption and how long does it take to intern strings
//        //in the second method call.
//        test (1000 * 1000);
//    }
//
//    private static void test (final int cnt) {
//        final List<String> lst = new ArrayList<String> (100);
//        long start = System.currentTimeMillis ();
//        for (int i = 0; i < cnt; ++i) {
//            final String str = "Very long test string, which tells you about something " +
//                               "very-very important, definitely deserving to be interned #" + i;
////uncomment the following line to test dependency from string length
////            final String str = Integer.toString( i );
//            lst.add (str.intern ());
//            if (i % 10000 == 0) {
//                System.out.println (i + "; time = " + (System.currentTimeMillis () - start) / 1000.0 + " sec");
//                start = System.currentTimeMillis ();
//            }
//        }
//        System.out.println ("Total length = " + lst.size ());
//    }
//
//    private static final WeakHashMap<String, WeakReference<String>> s_manualCache =
//            new WeakHashMap<String, WeakReference<String>> (100000);
//
//    private static String manualIntern (final String str) {
//        final WeakReference<String> cached = s_manualCache.get (str);
//        if (cached != null) {
//            final String value = cached.get ();
//            if (value != null) { return value; }
//        }
//        s_manualCache.put (str, new WeakReference<String> (str));
//        return str;
//    }
//
//    private static void testManual (final int cnt) {
//        final List<String> lst = new ArrayList<String> (100);
//        long start = System.currentTimeMillis ();
//        for (int i = 0; i < cnt; ++i) {
//            final String str = "Very long test string, which tells you about something " +
//                               "very-very important, definitely deserving to be interned #" + i;
//            lst.add (manualIntern (str));
//            if (i % 10000 == 0) {
//                System.out.println (i + "; manual time = " + (System.currentTimeMillis () - start) / 1000.0 + " sec");
//                start = System.currentTimeMillis ();
//            }
//        }
//        System.out.println ("Total length = " + lst.size ());
//    }
}

