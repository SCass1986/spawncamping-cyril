package org.stephen.hashmap.config;

public interface AppConfig {
    String getString (final String property, final String defaultValue);

    int getInteger (final String property, final DefaultValue<Integer> defaultValue);

    double getDouble (final String property, final DefaultValue<Double> defaultValue);

    boolean getBoolean (final String property, final DefaultValue<Boolean> defaultValue);
}

