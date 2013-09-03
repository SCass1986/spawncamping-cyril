package org.stephen.hashmap;

public interface Cache<ReturnType, KeyType> {
    ReturnType get (final KeyType key);
}

