package org.stephen.hashmap.caches.property;

import java.beans.PropertyDescriptor;

public enum PropertyHolderFactory {
    INSTANCE;

    public PropertyHolder create (final PropertyDescriptor propertyDescriptor) {
        return new PropertyHolder.Builder (propertyDescriptor.getName (), propertyDescriptor.getClass ())
                .withReadMethod (propertyDescriptor.getReadMethod ())
                .withWriteMethod (propertyDescriptor.getWriteMethod ())
                .build ();
    }
}
