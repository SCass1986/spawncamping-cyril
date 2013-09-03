package org.stephen.hashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EvictBySize implements EvictionStrategy<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> {
    private final int maxSize;

    public EvictBySize (final int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean evictEntry (final LinkedHashMap<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> map, final Map.Entry<PropertyKeyFactory.PropertyKey, AbstractPropertyCache.PropertyHolder> entry) {
//        System.out.println (String.format ("Evicting Eldest Member (%s --> %s)? %s %s", entry.getKey (), entry.getValue ().getPropertyName (), "Size " + map.size () + ", Max " + maxSize, (map.size () >= maxSize)));
        return map.size () >= maxSize;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
