package org.stephen.hashmap.config;

public interface ApplicationConfiguration {
    String getString (final String property);

    String getString (final String property, final String defaultValue);

    int getInt (final String property);

    int getInt (final String property, final int defaultValue);

    double getDouble (final String property);

    double getDouble (final String property, final double defaultValue);

    boolean getBoolean (final String property);

    boolean getBoolean (final String property, final boolean defaultValue);
}

