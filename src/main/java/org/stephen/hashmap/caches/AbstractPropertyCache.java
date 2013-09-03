package org.stephen.hashmap.caches;

import org.stephen.hashmap.caches.property.PropertyHolder;


public abstract class AbstractPropertyCache<KeyType> implements Cache<PropertyHolder, KeyType> {

    protected AbstractPropertyCache () {

    }

    @Override
    public abstract PropertyHolder get (final KeyType key);

}

