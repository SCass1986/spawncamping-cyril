package org.stephen.hashmap.caches.lru;

public interface HashMapCache<KeyType, ValueType> {
    ValueType get (final KeyType key);
}
