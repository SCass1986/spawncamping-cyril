package org.stephen.hashmap;

import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    private final Map<String, CacheValue> cache  = new HashMap<> ();
    private final SecureRandom            random = new SecureRandom ();

    public Cache () {
        for (int i = 0; i < 100; ++i) {
            cache.put (String.format ("%s.%s.%s", Main.class.getName (), createRandomString (15), createRandomString (75)), createCacheValue ());
        }
    }

    public CacheValue get (final String className, final String method, final String property) {
        final long createKeyStartTime = System.nanoTime ();
        final CacheKey cacheKey = CacheKeyMap.getInstance ().getKey (String.format ("%s.%s.%s".intern (), className, method, property));
        System.out.println ("Time to create key '" + cacheKey.toString () + "': " + (System.nanoTime () - createKeyStartTime) + " ns");
        final long getKeyStartTime = System.nanoTime ();
        final CacheValue cacheValue = cache.get (cacheKey.getKey ());
        System.out.println ("Time to get key '" + cacheKey.toString () + "'   : " + (System.nanoTime () - getKeyStartTime) + " ns");
        return cacheValue;
    }

    private CacheValue createCacheValue () {
        return new CacheValue (new BigInteger (45, random).toString (), random.nextLong (), random.nextDouble ());

    }

    public static String createRandomString (final int length) {
        return RandomStringUtils.random (length, true, true);
    }

    public static String createRandomString () {
        return RandomStringUtils.random (50, true, true);
    }

    private static final class CacheKeyMap {
        static final class CacheKeyMapInstance {
            public static final CacheKeyMap cacheKeyMapInstance = new CacheKeyMap ();
        }

        private final Map<String, CacheKey> lookUp;

        protected CacheKeyMap () {
            this.lookUp = new HashMap<> ();
        }

        public static CacheKeyMap getInstance () {
            return CacheKeyMapInstance.cacheKeyMapInstance;
        }

        public CacheKey getKey (final String key) {
            final CacheKey cacheKey = lookUp.get (key);
            return cacheKey != null ? cacheKey : put (key);
        }

        private CacheKey put (final String key) {
            final CacheKey cacheKey = new CacheKey (key);
            lookUp.put (cacheKey.getKey (), cacheKey);
            return cacheKey;
        }
    }

    private static final class CacheKey {
        private final String key;
        private final int    keyHashCode;

        public CacheKey (final String key) {
            this.key = key;
            this.keyHashCode = key.hashCode ();
        }

        public String getKey () {
            return key;
        }

        @Override
        public int hashCode () {
            return this.keyHashCode;
        }


        @Override
        public boolean equals (Object o) {
            if (this == o) { return true; }
            if (o == null || getClass () != o.getClass ()) { return false; }

            CacheKey cacheKey = (CacheKey) o;
            if (key != null ? !key.equals (cacheKey.key) : cacheKey.key != null) { return false; }
            return true;
        }

        @Override
        public String toString () {
            return this.key;
        }
    }

    private static final class CacheValue {
        private final String stringValue;
        private final long   longValue;
        private final double doubleValue;

        public CacheValue (final String stringValue, final long longValue, final double doubleValue) {
            this.stringValue = stringValue;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
        }

        String getStringValue () {
            return stringValue;
        }

        long getLongValue () {
            return longValue;
        }

        double getDoubleValue () {
            return doubleValue;
        }
    }
}
