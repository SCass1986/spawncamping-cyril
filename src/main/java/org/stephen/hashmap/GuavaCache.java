package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.PropertyKeyFactory.PropertyKey;


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
    public PropertyHolder get (final String property) {
        return propertyCache.getUnchecked (PropertyKeyFactory.INSTANCE.getKey (property.intern ()));
    }
}