package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.PropertyKeyFactory.PropertyKey;


public final class GuavaCache {
    private final LoadingCache<PropertyKey, PropertyHolder> propertyCache;

    public GuavaCache () {
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (100)
                                         .concurrencyLevel (1)
                                         .expireAfterAccess (100, TimeUnit.SECONDS)
                                         .build (new PropertyCacheLoader ());
    }

    public PropertyHolder get (final String key) {
        return propertyCache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));
    }

    public static final class PropertyHolder {
        private final String propertyName;
        private final String className;
        private final Method readMethod;
        private final Method writeMethod;
        private final int    hashCode;

        private PropertyHolder (final Builder builder) {
            this.propertyName = builder.propertyName;
            this.className = builder.className;
            this.readMethod = builder.readMethod;
            this.writeMethod = builder.writeMethod;
            this.hashCode = getHashCode ();
        }

        public String getPropertyName () {
            return propertyName;
        }

        public String getClassName () {
            return className;
        }

        public Method getWriteMethod () {
            return writeMethod;
        }

        public Method getReadMethod () {
            return readMethod;
        }

        @Override
        public int hashCode () {
            return hashCode;
        }

        private int getHashCode () {
            int result = propertyName != null ? propertyName.hashCode () : 0;
            result = 31 * result + (className != null ? className.hashCode () : 0);
            result = 31 * result + (readMethod != null ? readMethod.hashCode () : 0);
            result = 31 * result + (writeMethod != null ? writeMethod.hashCode () : 0);
            result = 31 * result + hashCode;
            return result;
        }

        @Override
        public boolean equals (final Object o) {
            if (this == o) { return true; }
            if (o == null || getClass () != o.getClass ()) { return false; }

            final PropertyHolder other = (PropertyHolder) o;

            if (hashCode != other.hashCode) { return false; }
            if (className != null ? !className.equals (other.className) : other.className != null) { return false; }
            if (propertyName != null ? !propertyName.equals (other.propertyName) : other.propertyName != null) {
                return false;
            }
            if (readMethod != null ? !readMethod.equals (other.readMethod) : other.readMethod != null) { return false; }
            if (writeMethod != null ? !writeMethod.equals (other.writeMethod) : other.writeMethod != null) {
                return false;
            }

            return true;
        }

        public static final class Builder {
            private final String propertyName;
            private final String className;
            private       Method readMethod;
            private       Method writeMethod;

            public Builder (final String propertyName, final String className) {
                this.propertyName = propertyName;
                this.className = className;
            }

            public Builder withReadMethod (final Method readMethod) {
                this.readMethod = readMethod;
                return this;
            }

            public Builder withWriteMethod (final Method writeMethod) {
                this.writeMethod = writeMethod;
                return this;
            }

            public PropertyHolder build () {
                return new PropertyHolder (this);
            }
        }
    }
}