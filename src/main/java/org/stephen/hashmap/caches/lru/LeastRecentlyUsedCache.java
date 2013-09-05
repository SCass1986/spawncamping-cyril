package org.stephen.hashmap.caches.lru;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.Logger;
import org.stephen.hashmap.Main;
import org.stephen.hashmap.PropertyDescriptorUtils;
import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.PropertyCacheBuilder;
import org.stephen.hashmap.caches.PropertyDescriptorCache;
import org.stephen.hashmap.caches.lru.eviction.EvictBySize;
import org.stephen.hashmap.caches.lru.eviction.EvictionStrategy;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyHolderFactory;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class LeastRecentlyUsedCache implements ClassPropertyCache<PropertyKeyFactory.PropertyKey, PropertyHolder> {
    private static int     INITIAL_CAPACITY = 16;
    private static float   LOAD_FACTOR      = 0.75f;
    private static boolean ACCESS_ORDER     = true;

    private final LinkedHashMapCache cache;

    protected LeastRecentlyUsedCache (final Builder builder) {
        this.cache = new LinkedHashMapCache (builder.initialCapacity, builder.loadFactor, builder.accessOrder, builder.evictionStrategy);
    }

    @Override
    public PropertyHolder get (final PropertyKeyFactory.PropertyKey key) {
        return cache.get (key);
    }

    public PropertyHolder get (final String key) {
        return cache.get (PropertyKeyFactory.INSTANCE.getKey (key.intern ()));  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clearCache () {
        cache.clear ();
    }

    @Override
    public Set<PropertyHolder> getValues () {
        return Sets.newHashSet (cache.values ());
    }

    @Override
    public Set<Map.Entry<PropertyKeyFactory.PropertyKey, PropertyHolder>> getEntries () {
        return Sets.newHashSet (cache.entrySet ());
    }

    private static final class LinkedHashMapCache extends LinkedHashMap<PropertyKeyFactory.PropertyKey, PropertyHolder> {
        private final PropertyDescriptorCache propertyDescriptorCache = new PropertyDescriptorCache ();
        private EvictionStrategy<PropertyKeyFactory.PropertyKey, PropertyHolder> evictionStrategy;

        public LinkedHashMapCache (final int initialCapacity, final float loadFactor, final boolean accessOrder, final EvictionStrategy evictionStrategy) {
            super (initialCapacity, loadFactor, accessOrder);
            setEvictionStrategy (evictionStrategy);
        }

        @Override
        public PropertyHolder get (final Object key) {
            try {
                final PropertyKeyFactory.PropertyKey propertyKey = PropertyKeyFactory.INSTANCE.getKey ((String) key);
                return get (propertyKey);
            } catch (Exception e) {
                return null;
            }
        }

        public PropertyHolder get (final PropertyKeyFactory.PropertyKey key) {
            try {
                final PropertyHolder propertyHolder = super.get (key);
                if (propertyHolder != null) {
                    Logger.debug ("LinkedHashMapCache: Got Property <%s>!", key.getKey ());
                    return propertyHolder;
                }
                Logger.debug ("LinkedHashMapCache: Putting Property <%s> In Cache!", key.getKey ());
                return getPropertyHolder (key);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public PropertyHolder put (final PropertyKeyFactory.PropertyKey key, final PropertyHolder value) {
            super.put (key, value);
            return value;//To change body of overridden methods use File | Settings | File Templates.
        }

        protected void setEvictionStrategy (final EvictionStrategy evictionStrategy) {
            this.evictionStrategy = evictionStrategy;
            this.evictionStrategy.setLinkedHashMap (this);
        }

        @Override
        protected boolean removeEldestEntry (final Map.Entry<PropertyKeyFactory.PropertyKey, PropertyHolder> eldest) {
            if (Main.LOGGING_DEBUG_ON) {
                final boolean evict = evictionStrategy.evictEntry (eldest);
                Logger.debug ("Evicting Entry <%s>? %s (Entries: %s)", eldest.getKey ().getKey (), evict, size ());
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

    public static final class Builder implements PropertyCacheBuilder<LeastRecentlyUsedCache> {
        private int              initialCapacity;
        private float            loadFactor;
        private boolean          accessOrder;
        private EvictionStrategy evictionStrategy;

        public Builder () {
        }

        public Builder withInitialCapacity (final int initialCapacity) {
            this.initialCapacity = initialCapacity;
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

        @Override
        public Builder withDefaults () {
            this.initialCapacity = INITIAL_CAPACITY;
            this.loadFactor = LOAD_FACTOR;
            this.accessOrder = ACCESS_ORDER;
            this.evictionStrategy = new EvictBySize (INITIAL_CAPACITY * 2);
            return this;
        }

        @Override
        public LeastRecentlyUsedCache build () {
            return new LeastRecentlyUsedCache (this);
        }
    }
}
