package org.stephen.hashmap;

import com.google.common.cache.CacheLoader;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.concurrent.ExecutionException;

import static org.stephen.hashmap.GuavaCache.PropertyHolder;

public final class PropertyCacheLoader extends CacheLoader<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> {
    private final PropertyDescriptorCache propertyDescriptorCache;
    private final PropertyDescriptorUtils util = new PropertyDescriptorUtils ();

    public PropertyCacheLoader () {
        super ();
        this.propertyDescriptorCache = new PropertyDescriptorCache ();
    }

    @Override
    public PropertyHolder load (final PropertyKeyFactory.PropertyKey property) throws Exception {
        final String propertyKey = property.getKey ();
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