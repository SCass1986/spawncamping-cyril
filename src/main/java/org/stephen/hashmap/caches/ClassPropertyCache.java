package org.stephen.hashmap.caches;

public interface ClassPropertyCache<KeyType, ValueType> {
    ValueType get (final KeyType key);
}

