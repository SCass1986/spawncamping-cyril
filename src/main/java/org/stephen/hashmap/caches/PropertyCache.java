package org.stephen.hashmap.caches;

import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public class PropertyCache implements ClassUtil<PropertyKey, PropertyHolder> {
    private final ClassPropertyCache<PropertyKey, PropertyHolder> cache;

    public PropertyCache (final ClassPropertyCache<PropertyKey, PropertyHolder> cache) {
        this.cache = cache;
    }

    @Override
    public Object getValue (final Object object, final String property) {
        final PropertyKey key = PropertyKeyFactory.INSTANCE.getKey (String.format ("%s.%s", object.getClass ().getName (), property));
        return cache.get (key).getValue (object);
    }
}

