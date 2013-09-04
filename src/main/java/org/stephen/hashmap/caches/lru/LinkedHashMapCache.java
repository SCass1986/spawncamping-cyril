package org.stephen.hashmap.caches.lru;

import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.PropertyDescriptorCache;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.lru.eviction.EvictionStrategy;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyHolderFactory;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.stephen.hashmap.caches.property.PropertyKeyFactory.PropertyKey;

public final class LinkedHashMapCache implements ClassPropertyCache<String, PropertyHolder> {
    private final HashMapCache cache;

    protected LinkedHashMapCache (final Builder builder) {
        this.cache = new HashMapCache (builder.initialCapactity, builder.loadFactor, builder.accessOrder, builder.evictionStrategy);
    }

    @Override
    public PropertyHolder get (final String key) {
        return cache.get (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static final class HashMapCache extends LinkedHashMap<PropertyKey, PropertyHolder> {
        private final PropertyDescriptorCache propertyDescriptorCache = new PropertyDescriptorCache ();
        private final PropertyDescriptorUtils util                    = new PropertyDescriptorUtils ();
        private EvictionStrategy<PropertyKey, PropertyHolder> evictionStrategy;

        public HashMapCache (final int initialCapacity, final float loadFactor, final boolean accessOrder, final EvictionStrategy evictionStrategy) {
            super (initialCapacity, loadFactor, accessOrder);
            setEvictionStrategy (evictionStrategy);
        }

        @Override
        public PropertyHolder get (final Object key) {
            final PropertyKey propertyKey = (PropertyKey) key;
            return get (propertyKey);
        }

        public PropertyHolder get (final PropertyKey key) {
            try {
                final PropertyHolder propertyHolder = super.get (key);
                return propertyHolder != null ? propertyHolder : getPropertyHolder (key);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public PropertyHolder put (final PropertyKey key, final PropertyHolder value) {
            super.put (key, value);
            return value;//To change body of overridden methods use File | Settings | File Templates.
        }

        public void setEvictionStrategy (final EvictionStrategy evictionStrategy) {
            this.evictionStrategy = evictionStrategy;
            this.evictionStrategy.setLinkedHashMap (this);
        }

        private PropertyHolder getPropertyHolder (final PropertyKey key) throws ClassNotFoundException, IntrospectionException {
            final String property = key.getKey ();
            final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (util.getPropertyFromKeyString (property),
                                                                                         util.getClassFromKeyString (property));
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

        @Override
        protected boolean removeEldestEntry (final Map.Entry<PropertyKey, PropertyHolder> eldest) {
            return evictionStrategy.evictEntry (eldest);
        }

    }

    public static final class Builder {
        private int              initialCapactity;
        private float            loadFactor;
        private boolean          accessOrder;
        private EvictionStrategy evictionStrategy;

        public Builder () {
        }

        public Builder withInitialCapacity (final int initialCapacity) {
            this.initialCapactity = initialCapacity;
            return this;
        }

        public Builder withLoadFactor (final float loadFactor) {
            this.loadFactor = loadFactor;
            return this;
        }

        public Builder withAccessOrder (final boolean accessOrder) {
            this.accessOrder = accessOrder;
            return this;
        }

        public Builder withEvictionStrategy (final EvictionStrategy evictionStrategy) {
            this.evictionStrategy = evictionStrategy;
            return this;
        }

        public LinkedHashMapCache build () {
            return new LinkedHashMapCache (this);
        }
    }
}
