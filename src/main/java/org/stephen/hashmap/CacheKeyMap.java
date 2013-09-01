package org.stephen.hashmap;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public enum CacheKeyMap {
    INSTANCE;

    private static final String KEY_SPLIT_TOKEN = ".";

    private final Map<String, WeakReference<CacheKey>> lookUp = new WeakHashMap<> ();

    public CacheKey getKey (final String key) throws ClassNotFoundException {
        final WeakReference<CacheKey> cacheKey = lookUp.get (key);
        if (cacheKey != null) {
            return cacheKey.get ();
        }

        return putKey (key);
    }

    private CacheKey putKey (final String key) throws ClassNotFoundException {
        final Class<?> clazz = getClassFromKeyString (key);
        final String property = getPropertyFromKeyString (key);
        final WeakReference<CacheKey> cacheKeyWeakReference = new WeakReference<> (new CacheKey (clazz, property));
        lookUp.put (key, cacheKeyWeakReference);
        return cacheKeyWeakReference.get ();
    }

    private Class<?> getClassFromKeyString (final String key) throws ClassNotFoundException {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return Class.forName (StringUtils.substring (key, 0, splitIndex));
    }

    private String getPropertyFromKeyString (final String key) {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return StringUtils.substring (key, splitIndex + 1);
    }

    static final class CacheKey {
        private final String   key;
        private final Class<?> clazz;
        private final int      hashCode;

        public CacheKey (final Class<?> clazz, final String key) {
            this.clazz = clazz;
            this.key = key;
            this.hashCode = getHashCode ();
        }

        public Class<?> getClassType () {
            return clazz;
        }

        public String getProperty () {
            return key;
        }

        @Override
        public boolean equals (Object o) {
            if (this == o) { return true; }
            if (o == null || getClass () != o.getClass ()) { return false; }

            CacheKey cacheKey = (CacheKey) o;

            if (clazz != null ? !clazz.equals (cacheKey.clazz) : cacheKey.clazz != null) { return false; }
            if (key != null ? !key.equals (cacheKey.key) : cacheKey.key != null) { return false; }

            return true;
        }

        @Override
        public int hashCode () {
            return this.hashCode;
        }

        private int getHashCode () {
            int result = key != null ? key.hashCode () : 0;
            result = 31 * result + (clazz != null ? clazz.hashCode () : 0);
            return result;
        }

        @Override
        public String toString () {
            return String.format ("%s.%s", clazz.getName (), key);
        }
    }

}
