package org.stephen.hashmap;

public enum PropertyKeyFactory {
    INSTANCE;

    public PropertyKey getKey (final String key) {
        return new PropertyKey (key.intern ());
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
