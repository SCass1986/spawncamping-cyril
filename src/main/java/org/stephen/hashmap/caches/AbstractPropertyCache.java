package org.stephen.hashmap.caches;

import org.stephen.hashmap.caches.property.PropertyHolder;


public abstract class AbstractPropertyCache<KeyType> implements ClassPropertyCache<KeyType, PropertyHolder> {

    protected AbstractPropertyCache () {

    }

    @Override
    public abstract PropertyHolder get (final KeyType key);

}

