package org.stephen.hashmap.caches.lru;

import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.Logger;
import org.stephen.hashmap.Main;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.PropertyDescriptorCache;
import org.stephen.hashmap.caches.lru.eviction.EvictionStrategy;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyHolderFactory;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;

public final class LinkedHashMapCache extends LinkedHashMap<PropertyKey, PropertyHolder> {
    private final PropertyDescriptorCache propertyDescriptorCache = new PropertyDescriptorCache ();
    private EvictionStrategy<PropertyKey, PropertyHolder> evictionStrategy;

    public LinkedHashMapCache (final int initialCapacity, final float loadFactor, final boolean accessOrder, final EvictionStrategy evictionStrategy) {
        super (initialCapacity, loadFactor, accessOrder);
        setEvictionStrategy (evictionStrategy);
    }

    @Override
    public PropertyHolder get (final Object key) {
        try {
            final PropertyKey propertyKey = PropertyKeyFactory.INSTANCE.getKey ((String) key);
            return get (propertyKey);
        } catch (Exception e) {
            return null;
        }
    }

    public PropertyHolder get (final PropertyKey key) {
        try {
            final PropertyHolder propertyHolder = super.get (key);
            if (propertyHolder != null) {
                Logger.info ("LinkedHashMapCache: Got Property <%s>!", key.getKey ());
                return propertyHolder;
            }
            Logger.info ("LinkedHashMapCache: Putting Property <%s> In Cache!", key.getKey ());
            return getPropertyHolder (key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PropertyHolder put (final PropertyKey key, final PropertyHolder value) {
        super.put (key, value);
        return value;//To change body of overridden methods use File | Settings | File Templates.
    }

    protected void setEvictionStrategy (final EvictionStrategy evictionStrategy) {
        this.evictionStrategy = evictionStrategy;
        this.evictionStrategy.setLinkedHashMap (this);
    }

    @Override
    protected boolean removeEldestEntry (final Map.Entry<PropertyKeyFactory.PropertyKey, PropertyHolder> eldest) {
        if (Main.LOGGING_ON) {
            final boolean evict = evictionStrategy.evictEntry (eldest);
            Logger.info ("Evicting Entry <%s>? %s (Entries: %s)", eldest.getKey ().getKey (), evict, size ());
        }
        return evictionStrategy.evictEntry (eldest);
    }

    private PropertyHolder getPropertyHolder (final PropertyKeyFactory.PropertyKey key) throws ClassNotFoundException, IntrospectionException {
        final String keyString = key.getKey ();

        final String property = PropertyDescriptorUtils.getPropertyFromKeyString (keyString);
        final Class<?> clazz = PropertyDescriptorUtils.getClassFromKeyString (keyString);

        final PropertyDescriptor[] propertyDescriptors = propertyDescriptorCache.get (clazz);
        for (final PropertyDescriptor prop : propertyDescriptors) {
            if (StringUtils.equals (prop.getName (), property)) {
            }
            return put (key, PropertyHolderFactory.INSTANCE.create (prop));
        }
        return null;
    }
}
