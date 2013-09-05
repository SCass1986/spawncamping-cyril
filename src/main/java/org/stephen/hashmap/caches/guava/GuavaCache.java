package org.stephen.hashmap.caches.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.PropertyCacheBuilder;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public final class GuavaCache implements ClassPropertyCache<PropertyKey, PropertyHolder> {
    private final LoadingCache<PropertyKey, PropertyHolder> cache;

    protected GuavaCache (final Builder builder) {
        super ();
        this.cache = CacheBuilder.newBuilder ()
                                 .maximumSize (builder.maximumSize)
                                 .concurrencyLevel (builder.concurrencyLevel)
                                 .expireAfterAccess (builder.expireAfterAccessTime, builder.expireAfterAccessTimeUnit)
                                 .build (new PropertyCacheLoader ());
    }

    @Override
    public PropertyHolder get (final PropertyKey key) {
        return cache.getUnchecked (key);
    }

    @Override
    public PropertyHolder get (final String key) {
        return cache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));
    }

    @Override
    public void clearCache () {
        cache.cleanUp ();
    }

    @Override
    public Set<PropertyHolder> getValues () {
        return Sets.newHashSet (cache.asMap ().values ());
    }

    @Override
    public Set<Map.Entry<PropertyKey, PropertyHolder>> getEntries () {
        return Sets.newHashSet (cache.asMap ().entrySet ());
    }

    public static final class Builder implements PropertyCacheBuilder<GuavaCache> {
        private int      maximumSize;
        private int      concurrencyLevel;
        private int      expireAfterAccessTime;
        private TimeUnit expireAfterAccessTimeUnit;

        public Builder () {
        }

        public Builder withMaximumSize (final int maximumSize) {
            this.maximumSize = maximumSize;
            return this;
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

        @Override
        public Builder withDefaults () {
            this.maximumSize = 32;
            this.concurrencyLevel = 1;
            this.expireAfterAccessTime = 10;
            this.expireAfterAccessTimeUnit = TimeUnit.MINUTES;
            return this;
        }

        @Override
        public GuavaCache build () {
            return new GuavaCache (this);
        }
    }
}