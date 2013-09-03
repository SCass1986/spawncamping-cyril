package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.stephen.hashmap.caches.Cache;

import java.beans.PropertyDescriptor;
import java.util.concurrent.TimeUnit;

public final class PropertyDescriptorCache implements Cache<PropertyDescriptor[], Class<?>> {
    private final LoadingCache<Class<?>, PropertyDescriptor[]> propertyDescriptorCache;
    private final PropertyDescriptorUtils util = new PropertyDescriptorUtils ();

    public PropertyDescriptorCache () {
        this.propertyDescriptorCache = CacheBuilder.newBuilder ()
                                                   .maximumSize (100)
                                                   .concurrencyLevel (1)
                                                   .expireAfterAccess (10, TimeUnit.MINUTES)
                                                   .build (new PropertyDescriptorCacheLoader (util));

    }

    @Override
    public PropertyDescriptor[] get (final Class<?> key) {
        return propertyDescriptorCache.getUnchecked (key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    static final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, PropertyDescriptor[]> {

        private final PropertyDescriptorUtils util;

        public PropertyDescriptorCacheLoader (final PropertyDescriptorUtils util) {
            super ();
            this.util = util;
        }

        @Override
        public PropertyDescriptor[] load (final Class<?> key) throws Exception {
            return util.getPropertyDescriptors (key);
        }
    }
}
