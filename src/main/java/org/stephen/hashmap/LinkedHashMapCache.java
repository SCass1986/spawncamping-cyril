package org.stephen.hashmap;

import org.apache.commons.lang3.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.stephen.hashmap.PropertyKeyFactory.PropertyKey;

public final class LinkedHashMapCache extends AbstractPropertyCache<String> {
    private final HashMapCache cache;

    public LinkedHashMapCache () {
        this.cache = new HashMapCache (new EvictBySize (5));
    }

    @Override
    public PropertyHolder get (final String property) {
        return cache.get (PropertyKeyFactory.INSTANCE.getKey (property.intern ()));  //To change body of implemented methods use File | Settings | File Templates.
    }

    static final class HashMapCache extends LinkedHashMap<PropertyKey, PropertyHolder> {

        private static final int     INITIAL_CAPACITY = 16;
        private static final float   LOAD_FACTOR      = 0.75f;
        private static final boolean ACCESS_ORDER     = true;

        private final PropertyDescriptorCache                       propertyDescriptorCache;
        private final EvictionStrategy<PropertyKey, PropertyHolder> evictionStrategy;
        private final PropertyDescriptorUtils util = new PropertyDescriptorUtils ();

        public HashMapCache (final EvictionStrategy evictionStrategy) {
            super (INITIAL_CAPACITY, LOAD_FACTOR, ACCESS_ORDER);
            this.evictionStrategy = evictionStrategy;
            this.propertyDescriptorCache = new PropertyDescriptorCache ();
        }

        @Override
        public PropertyHolder get (final Object key) {
            try {
                final PropertyKey propertyKey = (PropertyKey) key;
                final PropertyHolder propertyHolder = super.get (propertyKey);
                return propertyHolder != null ? propertyHolder : getPropertyHolder (propertyKey.getKey ());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public PropertyHolder put (final PropertyKey key, final PropertyHolder value) {
            super.put (key, value);
            return value;//To change body of overridden methods use File | Settings | File Templates.
        }

        private PropertyHolder getPropertyHolder (final String property) throws ClassNotFoundException, IntrospectionException {
            final PropertyDescriptor descriptor = getPropertyFromPropertyDescriptorList (util.getPropertyFromKeyString (property),
                                                                                         util.getClassFromKeyString (property));
            return put (PropertyKeyFactory.INSTANCE.getKey (property), util.createPropertyHolder (descriptor));
        }

        private PropertyDescriptor getPropertyFromPropertyDescriptorList (final String property, final Class<?> clazz) throws IntrospectionException {
            final PropertyDescriptor[] propertyDescriptors = propertyDescriptorCache.get (clazz);
            for (final PropertyDescriptor prop : propertyDescriptors) {
                if (StringUtils.equals (prop.getName (), property)) {
                    return prop;
                }
            }
            return null;
        }

        @Override
        protected boolean removeEldestEntry (final Map.Entry<PropertyKey, PropertyHolder> eldest) {
            return evictionStrategy.evictEntry (this, eldest);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}