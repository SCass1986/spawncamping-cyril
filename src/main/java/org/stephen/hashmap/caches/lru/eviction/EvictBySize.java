package org.stephen.hashmap.caches.lru.eviction;

import org.stephen.hashmap.caches.property.PropertyHolder;
import org.stephen.hashmap.caches.property.PropertyKeyFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EvictBySize implements EvictionStrategy<PropertyKeyFactory.PropertyKey, PropertyHolder> {
    private final int           maxSize;
    private       LinkedHashMap map;

    public EvictBySize (final int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public void setLinkedHashMap (final LinkedHashMap map) {
        this.map = map;
    }

    @Override
    public boolean evictEntry (final Map.Entry<PropertyKeyFactory.PropertyKey, PropertyHolder> entry) {
        return map.size () >= maxSize;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
