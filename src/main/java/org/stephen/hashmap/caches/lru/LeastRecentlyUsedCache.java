package org.stephen.hashmap.caches.lru;

import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.lru.eviction.EvictBySize;
import org.stephen.hashmap.caches.lru.eviction.EvictionStrategy;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

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
    }

    public static final class Builder {
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

        public Builder withDefaults () {
            this.initialCapacity = INITIAL_CAPACITY;
            this.loadFactor = LOAD_FACTOR;
            this.accessOrder = ACCESS_ORDER;
            this.evictionStrategy = new EvictBySize (INITIAL_CAPACITY * 2);
            return this;
        }

        public LeastRecentlyUsedCache build () {
            return new LeastRecentlyUsedCache (this);
        }
    }
}
