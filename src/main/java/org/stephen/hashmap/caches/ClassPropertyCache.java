package org.stephen.hashmap.caches;

public interface ClassPropertyCache<KeyType, ValueType> {
    ValueType get (final KeyType key);

    ValueType get (final String key) throws ClassNotFoundException;

    void clearCache ();
}

