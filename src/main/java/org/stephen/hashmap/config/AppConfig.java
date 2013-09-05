package org.stephen.hashmap.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

public enum AppConfig implements ApplicationConfiguration {
    INSTANCE;

    private AppConfig () {
    }

    @Override
    public String getString (final String property) {
        return Properties.properties.getString (property, null);
    }

    @Override
    public int getInt (final String property) {
        return Properties.properties.getInt (property);
    }

    @Override
    public double getDouble (final String property) {
        return Properties.properties.getDouble (property);
    }

    @Override
    public boolean getBoolean (final String property) {
        return Properties.properties.getBoolean (property);
    }

    @Override
    public String getString (final String property, final String defaultValue) {
        return Properties.properties.getString (property, defaultValue);
    }

    @Override
    public int getInt (final String property, final int defaultValue) {
        return Properties.properties.getInt (property, defaultValue);
    }

    @Override
    public double getDouble (final String property, final double defaultValue) {
        return Properties.properties.getDouble (property, defaultValue);
    }

    @Override
    public boolean getBoolean (final String property, final boolean defaultValue) {
        return Properties.properties.getBoolean (property, defaultValue);
    }

    private static final class Properties {
        private static final   String                  CACHE_PROPERTIES_FILE = "cache.properties";
        protected static final PropertiesConfiguration properties            = new PropertiesConfiguration ();

        static {
            try {
                properties.load (CACHE_PROPERTIES_FILE);
                properties.setReloadingStrategy (new FileChangedReloadingStrategy ());
            } catch (ConfigurationException e) {
                e.printStackTrace ();
            }
        }
    }
}
