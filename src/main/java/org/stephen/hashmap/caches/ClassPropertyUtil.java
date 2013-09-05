package org.stephen.hashmap.caches;

import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.util.Map;
import java.util.Set;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;


public final class ClassPropertyUtil implements ClassUtil, ClassPropertyCache<PropertyKey, PropertyHolder> {
    private final ClassPropertyCache<PropertyKey, PropertyHolder> cache;

    public ClassPropertyUtil (final ClassPropertyCache<PropertyKey, PropertyHolder> cache) {
        this.cache = cache;
    }

    @Override
    public Object getValue (final Object object, final String property) {
        final PropertyKey key = PropertyKeyFactory.INSTANCE.getKey (String.format ("%s.%s", object.getClass ().getName ().intern (), property));
        return cache.get (key).getValue (object);
    }

    @Override
    public PropertyHolder get (final PropertyKey key) {
        return cache.get (key);
    }

    @Override
    public PropertyHolder get (final String key) throws ClassNotFoundException {
        return cache.get (key);
    }

    @Override
    public void clearCache () {
        cache.clearCache ();
    }

    @Override
    public Set<PropertyHolder> getValues () {
        return cache.getValues ();
    }

    @Override
    public Set<Map.Entry<PropertyKey, PropertyHolder>> getEntries () {
        return cache.getEntries ();
    }
}

