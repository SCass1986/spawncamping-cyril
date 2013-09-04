package org.stephen.hashmap.caches.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public final class GuavaCache implements ClassPropertyCache<String, PropertyHolder> {
    private final LoadingCache<PropertyKey, PropertyHolder> propertyCache;

    protected GuavaCache (final Builder builder) {
        super ();
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (builder.maximumSize)
                                         .concurrencyLevel (builder.concurrencyLevel)
                                         .expireAfterAccess (builder.expireAfterAccessTime, builder.expireAfterAccessTimeUnit)
                                         .build (new PropertyCacheLoader ());
    }

    @Override
    public PropertyHolder get (final String key) {
        return propertyCache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));
    }

    public static final class Builder {
        private final int maximumSize;
        private int      concurrencyLevel          = 1;
        private int      expireAfterAccessTime     = 100;
        private TimeUnit expireAfterAccessTimeUnit = TimeUnit.MINUTES;

        public Builder (final int maximumSize) {
            this.maximumSize = maximumSize;
        }

        public Builder withConcurrencyLevel (final int concurrencyLevel) {
            this.concurrencyLevel = concurrencyLevel;
            return this;
        }

        public Builder withExpireAfterAccessTime (final int expireAfterAccessTime) {
            this.expireAfterAccessTime = expireAfterAccessTime;
            return this;
        }

        public Builder withExpireAfterAccessTimeUnit (final TimeUnit expireAfterAccessTimeUnit) {
            this.expireAfterAccessTimeUnit = expireAfterAccessTimeUnit;
            return this;
        }

        public GuavaCache build () {
            return new GuavaCache (this);
        }
    }
}