package org.stephen.hashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public interface EvictionStrategy<KeyType, ValueType> {
    void setLinkedHashMap (final LinkedHashMap map);

    boolean evictEntry (final Map.Entry<KeyType, ValueType> entry);
}
