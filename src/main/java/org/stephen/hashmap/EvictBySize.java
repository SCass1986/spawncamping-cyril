package org.stephen.hashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EvictBySize implements EvictionStrategy<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> {
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
    public boolean evictEntry (final Map.Entry<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> entry) {
        return map.size () >= maxSize;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
