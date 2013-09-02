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

public final class PropertyCacheLoader extends CacheLoader<String, PropertyHolder> {
    private static final String KEY_SPLIT_TOKEN = ".";

    private final LoadingCache<Class<?>, ArrayList<PropertyDescriptor>> propertyDescriptorCache;

    public PropertyCacheLoader () {
        this.propertyDescriptorCache = CacheBuilder.newBuilder ()
                                                   .maximumSize (10)
                                                   .expireAfterAccess (10, TimeUnit.MINUTES)
                                                   .build (new PropertyDescriptorCacheLoader ());
    }

    @Override
    public PropertyHolder load (final String property) throws Exception {
        return getProperty (property);  //To change body of implemented methods use File | Settings | File Templates.
    }

    private PropertyHolder getProperty (final String property) throws IntrospectionException, ExecutionException, ClassNotFoundException {
        final String propertyFromKeyString = getPropertyFromKeyString (property);
        final Class<?> classFromKeyString = getClassFromKeyString (property);
        final List<PropertyDescriptor> propertyDescriptors = propertyDescriptorCache.get (classFromKeyString);
        final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (propertyDescriptors, propertyFromKeyString);
        return createPropertyHolder (descriptor);
    }

    private PropertyDescriptor getPropertyFromPropertyDescriptorList (final List<PropertyDescriptor> propertyDescriptors, final String property) {

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

    static final class PropertyDescriptorCacheLoader extends CacheLoader<Class<?>, ArrayList<PropertyDescriptor>> {

        @Override
        public ArrayList<PropertyDescriptor> load (final Class<?> key) throws Exception {
            final PropertyDescriptor[] descriptors = getPropertyDescriptors (key);
            final ArrayList<PropertyDescriptor> descriptorList = Lists.newArrayListWithExpectedSize (descriptors.length);
            descriptorList.addAll (Arrays.asList (descriptors));
            return descriptorList;
        }

        private PropertyDescriptor[] getPropertyDescriptors (final Class<?> clazz) throws IntrospectionException {
            BeanInfo classInfo = Introspector.getBeanInfo (clazz, Object.class);
            return classInfo.getPropertyDescriptors ();
        }
    }

}
