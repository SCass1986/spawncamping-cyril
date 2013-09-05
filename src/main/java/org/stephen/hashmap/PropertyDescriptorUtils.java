package org.stephen.hashmap;

import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public final class PropertyDescriptorUtils {
    private static final String KEY_SPLIT_TOKEN = ".";

    protected PropertyDescriptorUtils () {

    }

    public static Class<?> getClassFromKeyString (final String key) throws ClassNotFoundException {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return Class.forName (StringUtils.substring (key, 0, splitIndex));
    }

    public static String getPropertyFromKeyString (final String key) {
        final int splitIndex = StringUtils.lastIndexOf (key, KEY_SPLIT_TOKEN);
        return StringUtils.substring (key, splitIndex + 1);
    }

    public static PropertyDescriptor[] getPropertyDescriptors (final Class<?> startClass) throws IntrospectionException {
        return getPropertyDescriptors (startClass, Object.class);
    }

    public static PropertyDescriptor[] getPropertyDescriptors (final Class<?> startClass, final Class<?> endClass) throws IntrospectionException {
        final BeanInfo classInfo = Introspector.getBeanInfo (startClass, endClass);
        return classInfo.getPropertyDescriptors ();
    }
}
