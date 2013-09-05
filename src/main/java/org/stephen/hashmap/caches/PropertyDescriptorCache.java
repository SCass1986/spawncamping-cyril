package org.stephen.hashmap.caches;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.stephen.hashmap.PropertyDescriptorUtils;

import java.beans.PropertyDescriptor;
import java.util.concurrent.TimeUnit;

public final class PropertyDescriptorCache implements ClassPropertyCache<Class<?>, PropertyDescriptor[]> {
    private final LoadingCache<Class<?>, PropertyDescriptor[]> propertyDescriptorCache;

    public PropertyDescriptorCache () {
        this.propertyDescriptorCache = CacheBuilder.newBuilder ()
                                                   .maximumSize (100)
                                                   .concurrencyLevel (1)
                                                   .expireAfterAccess (10, TimeUnit.MINUTES)
                                                   .build (new PropertyDescriptorCacheLoader ());

    }

    @Override
    public PropertyDescriptor[] get (final Class<?> key) {
        return propertyDescriptorCache.getUnchecked (key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearCache () {
        propertyDescriptorCache.cleanUp ();
    }

    static final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, PropertyDescriptor[]> {

        @Override
        public PropertyDescriptor[] load (final Class<?> key) throws Exception {
            return PropertyDescriptorUtils.getPropertyDescriptors (key);
        }
    }
}
