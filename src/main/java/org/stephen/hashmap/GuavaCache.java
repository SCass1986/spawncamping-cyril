package org.stephen.hashmap;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCache {
    private LoadingCache<String, PropertyDescriptor> propertyCache;

    public GuavaCache () {
        this.propertyCache = CacheBuilder.newBuilder ()
                                         .maximumSize (100)
                                         .expireAfterAccess (100, TimeUnit.SECONDS)
                                         .build (new PropertyCacheLoader ());
    }

    public PropertyDescriptor get (final String key) throws ExecutionException {
        return propertyCache.get (key);
    }
}

final class PropertyCacheLoader extends CacheLoader<String, PropertyDescriptor> {
    private static final String KEY_SPLIT_TOKEN = ".";

    private final LoadingCache<Class<?>, ArrayList<PropertyDescriptor>> propertyDescriptorCache;

    public PropertyCacheLoader () {
        this.propertyDescriptorCache = CacheBuilder.newBuilder ()
                                                   .maximumSize (10)
                                                   .expireAfterAccess (10, TimeUnit.MINUTES)
                                                   .build (new PropertyDescriptorCacheLoader ());
    }

    @Override
    public PropertyDescriptor load (final String property) throws Exception {
        return getProperty (property);  //To change body of implemented methods use File | Settings | File Templates.
    }

    private PropertyDescriptor getProperty (final String property) throws IntrospectionException, ExecutionException, ClassNotFoundException {
        final String propertyFromKeyString = getPropertyFromKeyString (property);
        final Class<?> classFromKeyString = getClassFromKeyString (property);
        final List<PropertyDescriptor> propertyDescriptors = propertyDescriptorCache.get (classFromKeyString);
        return getPropertyFromPropertyDescriptorList (propertyDescriptors, propertyFromKeyString);
    }

    private PropertyDescriptor getPropertyFromPropertyDescriptorList (final List<PropertyDescriptor> propertyDescriptors, final String property) {

        for (final PropertyDescriptor prop : propertyDescriptors) {
            if (StringUtils.equals (prop.getName (), property)) {
                return prop;
            }
        }
        return null;
    }

    private Class<?> getClassFromKeyString (final String key) throws ClassNotFoundException {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return Class.forName (StringUtils.substring (key, 0, splitIndex));
    }

    private String getPropertyFromKeyString (final String key) {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return StringUtils.substring (key, splitIndex + 1);
    }

}

final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, ArrayList<PropertyDescriptor>> {

    @Override
    public ArrayList<PropertyDescriptor> load (final Class<?> key) throws Exception {
        final ArrayList<PropertyDescriptor> descriptorList = Lists.newArrayList ();
        descriptorList.addAll (Arrays.asList (getPropertyDescriptors (key)));
        return descriptorList;
    }

    private PropertyDescriptor[] getPropertyDescriptors (final Class<?> clazz) throws IntrospectionException {
        BeanInfo classInfo = Introspector.getBeanInfo (clazz, Object.class);
        return classInfo.getPropertyDescriptors ();
    }
}

