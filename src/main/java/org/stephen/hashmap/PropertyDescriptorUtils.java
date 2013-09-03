package org.stephen.hashmap;

import org.apache.commons.lang3.StringUtils;
import org.stephen.hashmap.caches.property.PropertyHolder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public final class PropertyDescriptorUtils {
    private static final String KEY_SPLIT_TOKEN = ".";

    public PropertyHolder createPropertyHolder (final PropertyDescriptor propertyDescriptor) {
        return new PropertyHolder.Builder (propertyDescriptor.getName (), propertyDescriptor.getClass ().getName ())
                .withReadMethod (propertyDescriptor.getReadMethod ())
                .withWriteMethod (propertyDescriptor.getWriteMethod ())
                .build ();
    }

    public Class<?> getClassFromKeyString (final String key) throws ClassNotFoundException {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return Class.forName (StringUtils.substring (key, 0, splitIndex));
    }

    public String getPropertyFromKeyString (final String key) {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return StringUtils.substring (key, splitIndex + 1);
    }

    public PropertyDescriptor[] getPropertyDescriptors (final Class<?> startClass) throws IntrospectionException {
        return getPropertyDescriptors (startClass, Object.class);
    }

    public PropertyDescriptor[] getPropertyDescriptors (final Class<?> startClass, final Class<?> endClass) throws IntrospectionException {
        final BeanInfo classInfo = Introspector.getBeanInfo (startClass, endClass);
        return classInfo.getPropertyDescriptors ();
    }
}
