package org.stephen.hashmap.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.net.URL;

public enum ApplicationConfig implements AppConfig {
    INSTANCE;

    private static final String CACHE_PROPERTIES_FILE = "cache.properties";
    private final ApplicationConfigManager appConfig;

    private ApplicationConfig () {
        this.appConfig = new ApplicationConfigManager ();
        this.appConfig.init (getPropertiesConfigFile ());
    }

    @Override
    public String getString (final String property, final String defaultValue) {
        return appConfig.getString (property, defaultValue);
    }

    @Override
    public int getInteger (final String property, final DefaultValue<Integer> defaultValue) {
        return appConfig.getInteger (property, defaultValue);
    }

    @Override
    public double getDouble (final String property, final DefaultValue<Double> defaultValue) {
        return appConfig.getDouble (property, defaultValue);
    }

    @Override
    public boolean getBoolean (final String property, final DefaultValue<Boolean> defaultValue) {
        return appConfig.getBoolean (property, defaultValue);
    }

    private URL getPropertiesConfigFile () {
        return this.getClass ().getClassLoader ().getResource (CACHE_PROPERTIES_FILE);
    }

    private static final class ApplicationConfigManager implements AppConfig {

        private final PropertiesConfiguration properties;

        public ApplicationConfigManager () {
            this.properties = new PropertiesConfiguration ();
        }

        public boolean init (final URL configFile) {
            try {
                synchronized (this) {
                    properties.load (configFile);
                    properties.setReloadingStrategy (new FileChangedReloadingStrategy ());
                }
            } catch (ConfigurationException e) {
                e.printStackTrace ();
                return false;
            }
            return true;
        }

        @Override
        public String getString (final String property, final String defaultValue) {
            return properties.getString (property, defaultValue);
        }

        @Override
        public int getInteger (final String property, final DefaultValue<Integer> defaultValue) {
            return properties.getInt (property, defaultValue.value ());
        }

        @Override
        public double getDouble (final String property, final DefaultValue<Double> defaultValue) {
            return properties.getDouble (property, defaultValue.value ());
        }

        @Override
        public boolean getBoolean (final String property, final DefaultValue<Boolean> defaultValue) {
            return properties.getBoolean (property, defaultValue.value ());
        }
    }
}
