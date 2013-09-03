package org.stephen.hashmap.caches.guava;

import com.google.common.cache.CacheLoader;
import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.PropertyDescriptorCache;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.beans.PropertyDescriptor;
import java.util.concurrent.ExecutionException;

public final class PropertyCacheLoader extends CacheLoader<PropertyKeyFactory.PropertyKey, PropertyHolder> {
    private final PropertyDescriptorCache propertyDescriptorCache;
    private final PropertyDescriptorUtils util = new PropertyDescriptorUtils ();

    public PropertyCacheLoader () {
        super ();
        this.propertyDescriptorCache = new PropertyDescriptorCache ();
    }

    @Override
    public PropertyHolder load (final PropertyKeyFactory.PropertyKey key) throws Exception {
        final String propertyKey = key.getKey ();
        final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (util.getPropertyFromKeyString (propertyKey),
                                                                                     util.getClassFromKeyString (propertyKey));
        return util.createPropertyHolder (descriptor);
    }

    private PropertyDescriptor getPropertyFromPropertyDescriptorList (final String property, final Class<?> clazz) throws ExecutionException {
        final PropertyDescriptor[] propertyDescriptors = propertyDescriptorCache.get (clazz);
        for (final PropertyDescriptor prop : propertyDescriptors) {
            if (StringUtils.equals (prop.getName (), property)) {
                return prop;
            }
        }
        return null;
    }
}