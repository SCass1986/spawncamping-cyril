package org.stephen.hashmap.caches;

public interface PropertyCacheBuilder<CacheType extends ClassPropertyCache> {
    PropertyCacheBuilder withDefaults ();

    CacheType build ();
}
