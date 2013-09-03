package org.stephen.hashmap;

import java.util.LinkedHashMap;
import java.util.Map;

public interface EvictionStrategy<KeyType, ValueType> {
    boolean evictEntry (final LinkedHashMap<KeyType, ValueType> map, final Map.Entry<KeyType, ValueType> entry);
}
