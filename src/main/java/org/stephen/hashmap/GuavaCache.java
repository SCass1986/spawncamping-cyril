package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public final class GuavaCache {
    private final LoadingCache<PropertyKeyFactory.PropertyKey, PropertyHolder> propertyCache;

    public GuavaCache () {
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (100)
                                         .concurrencyLevel (1)
                                         .expireAfterAccess (100, TimeUnit.SECONDS)
                                         .build (new PropertyCacheLoader ());
    }

    public PropertyHolder get (final String key) {
        return propertyCache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (key));
    }

    public static final class PropertyHolder {
        private final String propertyName;
        private final Method readMethod;
        private final Method writeMethod;
        private final int    hashCode;

        private PropertyHolder (final Builder builder) {
            this.propertyName = builder.propertyName;
            this.readMethod = builder.readMethod;
            this.writeMethod = builder.writeMethod;
            this.hashCode = getHashCode ();
        }

        public String getPropertyName () {
            return propertyName;
        }

        public Method getReadMethod () {
            return readMethod;
        }

        public Method getWriteMethod () {
            return writeMethod;
        }

        public boolean isReadMethodSet () {
            return this.readMethod != null;
        }

        public boolean isWriteMethodSet () {
            return this.writeMethod != null;
        }

        @Override
        public int hashCode () {
            return hashCode;
        }

        @Override
        public boolean equals (Object o) {
            if (this == o) { return true; }
            if (o == null || getClass () != o.getClass ()) { return false; }

            PropertyHolder that = (PropertyHolder) o;

            if (propertyName != null ? !propertyName.equals (that.propertyName) : that.propertyName != null) {
                return false;
            }
            if (!readMethod.equals (that.readMethod)) { return false; }
            if (!writeMethod.equals (that.writeMethod)) { return false; }

            return true;
        }

        private int getHashCode () {
            int result = propertyName != null ? propertyName.hashCode () : 0;
            result = 31 * result + (readMethod != null ? readMethod.hashCode () : 0);
            result = 31 * result + (writeMethod != null ? writeMethod.hashCode () : 0);
            return result;
        }

        public static final class Builder {
            private final String propertyName;
            private       Method readMethod;
            private       Method writeMethod;

            public Builder (final String propertyName) {
                this.propertyName = propertyName;
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