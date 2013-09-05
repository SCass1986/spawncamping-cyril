package org.stephen.hashmap.caches.lru;

import org.stephen.hashmap.caches.ClassPropertyCache;
import org.stephen.hashmap.caches.lru.eviction.EvictionStrategy;
import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

public final class LeastRecentlyUsedCache implements ClassPropertyCache<String, PropertyHolder> {
    private final LinkedHashMapCache cache;

    protected LeastRecentlyUsedCache (final Builder builder) {
        this.cache = new LinkedHashMapCache (builder.initialCapacity, builder.loadFactor, builder.accessOrder, builder.evictionStrategy);
    }

    @Override
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

        public LeastRecentlyUsedCache build () {
            return new LeastRecentlyUsedCache (this);
        }

    }
}
