package org.stephen.hashmap.config;

public interface AppConfig {
    String getString (final String property);

    String getString (final String property, final String defaultValue);

    int getInt (final String property);

    int getInt (final String property, final DefaultValue<Integer> defaultValue);

    double getDouble (final String property);

    double getDouble (final String property, final DefaultValue<Double> defaultValue);

    boolean getBoolean (final String property);

    boolean getBoolean (final String property, final DefaultValue<Boolean> defaultValue);
}

