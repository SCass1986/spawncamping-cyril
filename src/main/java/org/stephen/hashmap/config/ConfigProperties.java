package org.stephen.hashmap.config;

public enum ConfigProperties {
    GUAVA_CACHE_MAXSIZE ("guava.cache.max_size", new DefaultValue<> (100)),
    GUAVA_CONCURRENCY_LEVEL ("guava.cache.concurrency_level", new DefaultValue<> (1));

    private final String       key;
    private final DefaultValue defaultValue;

    private ConfigProperties (final String key, final DefaultValue defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey () {
        return key;
    }

    public DefaultValue getDefault () {
        return defaultValue;
    }
}
