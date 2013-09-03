package org.stephen.hashmap.caches.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.stephen.hashmap.caches.AbstractPropertyCache;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public final class GuavaCache extends AbstractPropertyCache<String> {
    private final LoadingCache<PropertyKey, PropertyHolder> propertyCache;

    public GuavaCache () {
        super ();
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (100)
                                         .concurrencyLevel (1)
                                         .expireAfterAccess (100, TimeUnit.SECONDS)
                                         .build (new PropertyCacheLoader ());
    }

    @Override
    public PropertyHolder get (final String key) {
        return propertyCache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));
    }
}