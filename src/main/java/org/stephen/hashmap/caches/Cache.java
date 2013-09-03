package org.stephen.hashmap.caches;

public interface Cache<ReturnType, KeyType> {
    ReturnType get (final KeyType key);
}

