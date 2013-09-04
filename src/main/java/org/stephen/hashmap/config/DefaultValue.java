package org.stephen.hashmap.config;

public final class DefaultValue<ValueType extends Object> {
    private final ValueType value;

    public DefaultValue () {
        this (null);
    }

    public DefaultValue (final ValueType value) {
        this.value = value;
    }

    public ValueType value () {
        return value;
    }

    public boolean isNull () {
        return value == null;
    }
}
