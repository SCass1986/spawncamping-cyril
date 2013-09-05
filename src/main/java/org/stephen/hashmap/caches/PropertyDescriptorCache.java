package org.stephen.hashmap.caches;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import org.stephen.hashmap.PropertyDescriptorUtils;

import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public final class PropertyDescriptorCache implements ClassPropertyCache<Class<?>, PropertyDescriptor[]> {
    private final LoadingCache<Class<?>, PropertyDescriptor[]> cache;

    public PropertyDescriptorCache () {
        this.cache = CacheBuilder.newBuilder ()
                                 .maximumSize (100)
                                 .concurrencyLevel (1)
                                 .expireAfterAccess (10, TimeUnit.MINUTES)
                                 .build (new PropertyDescriptorCacheLoader ());

    }

    @Override
    public PropertyDescriptor[] get (final Class<?> key) {
        return cache.getUnchecked (key);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PropertyDescriptor[] get (final String key) throws ClassNotFoundException {
        final Class<?> clazz = PropertyDescriptorUtils.getClassFromKeyString (key);
        return get (clazz);
    }

    @Override
    public void clearCache () {
        cache.cleanUp ();
    }

    @Override
    public Set<PropertyDescriptor[]> getValues () {
        return Sets.newHashSet (cache.asMap ().values ());
    }

    @Override
    public Set<Map.Entry<Class<?>, PropertyDescriptor[]>> getEntries () {
        return Sets.newHashSet (cache.asMap ().entrySet ());
    }

    static final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, PropertyDescriptor[]> {

        @Override
        public PropertyDescriptor[] load (final Class<?> key) throws Exception {
            return PropertyDescriptorUtils.getPropertyDescriptors (key);
        }
    }
}
