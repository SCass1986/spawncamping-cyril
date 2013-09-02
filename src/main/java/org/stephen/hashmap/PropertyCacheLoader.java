package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.stephen.hashmap.GuavaCache.PropertyHolder;

public final class PropertyCacheLoader extends CacheLoader<PropertyKeyFactory.PropertyKey, PropertyHolder> {
    private static final String KEY_SPLIT_TOKEN = ".";

    private final LoadingCache<Class<?>, PropertyDescriptor[]> propertyDescriptorCache;

    public PropertyCacheLoader () {
        this.propertyDescriptorCache = CacheBuilder.newBuilder ()
                                                   .maximumSize (100)
                                                   .concurrencyLevel (1)
                                                   .expireAfterAccess (10, TimeUnit.MINUTES)
                                                   .build (new PropertyDescriptorCacheLoader ());
    }

    @Override
    public PropertyHolder load (final PropertyKeyFactory.PropertyKey property) throws Exception {
        final String propertyKey = property.getKey ();
        final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (getPropertyFromKeyString (propertyKey), getClassFromKeyString (propertyKey));
        return createPropertyHolder (descriptor);
    }

    private PropertyDescriptor getPropertyFromPropertyDescriptorList (final String property, final Class<?> clazz) throws ExecutionException {
        final PropertyDescriptor[] propertyDescriptors = propertyDescriptorCache.get (clazz);
        for (final PropertyDescriptor prop : propertyDescriptors) {
            if (StringUtils.equals (prop.getName (), property)) {
                return prop;
            }
        }
        return null;
    }

    private PropertyHolder createPropertyHolder (final PropertyDescriptor propertyDescriptor) {
        return new PropertyHolder.Builder (propertyDescriptor.getName ())
                .withReadMethod (propertyDescriptor.getReadMethod ())
                .withWriteMethod (propertyDescriptor.getWriteMethod ())
                .build ();
    }

    private Class<?> getClassFromKeyString (final String key) throws ClassNotFoundException {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return Class.forName (StringUtils.substring (key, 0, splitIndex));
    }

    private String getPropertyFromKeyString (final String key) {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return StringUtils.substring (key, splitIndex + 1);
    }

    static final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, PropertyDescriptor[]> {

        @Override
        public PropertyDescriptor[] load (final Class<?> key) throws Exception {
            BeanInfo classInfo = Introspector.getBeanInfo (key, Object.class);
            return classInfo.getPropertyDescriptors ();
        }
    }
}
