package org.stephen.hashmap.caches.property;

import org.stephen.hashmap.Logger;

import java.util.HashMap;

public enum PropertyKeyFactory {
    INSTANCE;

    private final HashMap<String, PropertyKey> lookUp = new HashMap<> ();

    public PropertyKey getKey (final String key) {
        final PropertyKey propertyKey = lookUp.get (key);
        if (propertyKey != null) {
            Logger.debug ("Returning Property Key <%s> from look up", key);
            return propertyKey;
        }
        Logger.debug ("Caching and returning Property Key <%s>", key);
        return getPropertyKey (key);
    }

    private PropertyKey getPropertyKey (final String key) {
        final PropertyKey newPropertyKey = new PropertyKey (key.intern ());
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
            if (!(o instanceof PropertyKey)) { return false; }
            PropertyKey that = (PropertyKey) o;
            return key.equals (that.key);
        }
    }
}
