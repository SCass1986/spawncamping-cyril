package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class GuavaCache {
    private final LoadingCache<String, PropertyHolder> propertyCache;

    public GuavaCache () {
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (100)
                                         .concurrencyLevel (1)
                                         .expireAfterAccess (100, TimeUnit.SECONDS)
                                         .build (new PropertyCacheLoader ());
    }

    public PropertyHolder get (final String key) {
        return propertyCache.getUnchecked (key);
    }
}

final class PropertyHolder {
    private final String propertyName;
    private final Method readMethod;
    private final Method writeMethod;

    private PropertyHolder (final Builder builder) {
        this.propertyName = builder.propertyName;
        this.readMethod = builder.readMethod;
        this.writeMethod = builder.writeMethod;
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

