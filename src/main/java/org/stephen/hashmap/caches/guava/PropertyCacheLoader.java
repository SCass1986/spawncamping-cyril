package org.stephen.hashmap.caches.guava;

import com.google.common.cache.CacheLoader;
import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.PropertyDescriptorCache;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyHolderFactory;

import java.beans.PropertyDescriptor;
import java.util.concurrent.ExecutionException;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;

public final class PropertyCacheLoader extends CacheLoader<PropertyKey, PropertyHolder> {
    private final PropertyDescriptorCache propertyDescriptorCache;

    public PropertyCacheLoader () {
        super ();
        this.propertyDescriptorCache = new PropertyDescriptorCache ();
    }

    @Override
    public PropertyHolder load (final PropertyKey key) throws Exception {
        final String propertyKey = key.getKey ();
        final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (PropertyDescriptorUtils.getPropertyFromKeyString (propertyKey),
                                                                                     PropertyDescriptorUtils.getClassFromKeyString (propertyKey));
        return PropertyHolderFactory.INSTANCE.create (descriptor);
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