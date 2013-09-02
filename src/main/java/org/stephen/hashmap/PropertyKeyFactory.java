package org.stephen.hashmap;

import com.google.common.collect.Maps;

import java.util.Map;

public enum PropertyKeyFactory {
    INSTANCE;

    private final Map<String, PropertyKey> lookUp = Maps.newHashMap ();

    public PropertyKey getKey (final String key) {
        return getKeyFromLookUp (key);
    }

    private PropertyKey getKeyFromLookUp (final String key) {
        final PropertyKey propertyKey = lookUp.get (key);
        if (propertyKey != null) {
            return propertyKey;
        }

        final PropertyKey newPropertyKey = new PropertyKey (key);
        lookUp.put (key, newPropertyKey);
        return newPropertyKey;
    }

    public static final class PropertyKey {
        private final String key;
        private final int    hashCode;

        private PropertyKey (final String key) {
            this.key = key;
            this.hashCode = getHashCode ();
        }

        public String getKey () {
            return key;
        }

        @Override
        public int hashCode () {
            return hashCode;
        }

        private int getHashCode () {
            return key.hashCode ();
        }

        @Override
        public boolean equals (Object o) {
            if (this == o) { return true; }
            if (o == null || getClass () != o.getClass ()) { return false; }

            PropertyKey that = (PropertyKey) o;

            if (!key.equals (that.key)) { return false; }

            return true;
        }
    }
}
