package org.stephen.hashmap;

import com.google.common.collect.Maps;

import java.util.Map;

public enum PropertyKeyFactory {
    INSTANCE;

    private final Map<String, PropertyKey> lookUp = Maps.newHashMapWithExpectedSize (100);

    public PropertyKey getKey (final String key) {
        return getPropertyKey (key);
    }

    private PropertyKey getPropertyKey (final String key) {
        PropertyKey propertyKey = lookUp.get (key);
        if (propertyKey != null) {
            return propertyKey;
        }
        propertyKey = new PropertyKey (key);
        lookUp.put (key, propertyKey);
        return propertyKey;
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
            if (!(o instanceof PropertyKey)) { return false; }
            PropertyKey that = (PropertyKey) o;
            return key.equals (that.key);
        }
    }
}
