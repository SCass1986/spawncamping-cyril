package org.stephen.hashmap.caches.lru;

import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.Logger;
import org.stephen.hashmap.Main;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.ClassPropertyCache;
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

public final class LinkedHashMapCache implements ClassPropertyCache<PropertyKey, PropertyHolder> {
    private final LinkedHashMapCacheHolder cache;

    public LinkedHashMapCache (final int initialCapacity, final float loadFactor, final boolean accessOrder, final EvictionStrategy evictionStrategy) {
        this.cache = new LinkedHashMapCacheHolder (initialCapacity, loadFactor, accessOrder, evictionStrategy);
    }

    @Override
    public PropertyHolder get (final PropertyKey key) {
        return cache.get (key);
    }

    @Override
    public void clearCache () {
        cache.clear ();
    }

    private static final class LinkedHashMapCacheHolder extends LinkedHashMap<PropertyKey, PropertyHolder> {

        private final PropertyDescriptorCache propertyDescriptorCache = new PropertyDescriptorCache ();
        private EvictionStrategy<PropertyKey, PropertyHolder> evictionStrategy;

        public LinkedHashMapCacheHolder (final int initialCapacity, final float loadFactor, final boolean accessOrder, final EvictionStrategy<PropertyKey, PropertyHolder> evictionStrategy) {
            super (initialCapacity, loadFactor, accessOrder);
            setEvictionStrategy (evictionStrategy);
        }

        public void setEvictionStrategy (final EvictionStrategy evictionStrategy) {
            this.evictionStrategy = evictionStrategy;
            this.evictionStrategy.setLinkedHashMap (this);
        }

        @Override
        public PropertyHolder get (final Object key) {
            try {
                final PropertyKeyFactory.PropertyKey propertyKey = (PropertyKeyFactory.PropertyKey) key;
                final PropertyHolder propertyHolder = super.get (propertyKey);
                if (propertyHolder != null) {
                    if (Main.LOGGING_ON) {
                        Logger.info ("LinkedHashMapCache: Got Property <%s>!", propertyKey.getKey ());
                    }
                    return propertyHolder;
                }
                if (Main.LOGGING_ON) {
                    Logger.info ("LinkedHashMapCache: Putting Property <%s> In Cache!", propertyKey.getKey ());
                }
                return getPropertyHolder (propertyKey);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public PropertyHolder put (final PropertyKey key, final PropertyHolder value) {
            super.put (key, value);
            return value;//To change body of overridden methods use File | Settings | File Templates.
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
            final String property = key.getKey ();
            final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (PropertyDescriptorUtils.getPropertyFromKeyString (property),
                                                                                         PropertyDescriptorUtils.getClassFromKeyString (property));
            return put (key, PropertyHolderFactory.INSTANCE.create (descriptor));
        }

        private PropertyDescriptor getPropertyFromPropertyDescriptorList (final String property, final Class<?> clazz) throws IntrospectionException {
            final PropertyDescriptor[] propertyDescriptors = propertyDescriptorCache.get (clazz);
            for (final PropertyDescriptor prop : propertyDescriptors) {
                if (StringUtils.equals (prop.getName (), property)) {
                    return prop;
                }
            }
            return null;
        }

    }
}
