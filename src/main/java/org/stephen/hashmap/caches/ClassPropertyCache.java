package org.stephen.hashmap.caches;

import java.util.Map;
import java.util.Set;

public interface ClassPropertyCache<KeyType, ValueType> {
    ValueType get (final KeyType key);

    ValueType get (final String key) throws ClassNotFoundException;

    void clearCache ();

    Set<ValueType> getValues ();

    Set<Map.Entry<KeyType, ValueType>> getEntries ();
}

