package org.stephen.hashmap.caches.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class PropertyHolder {
    private final String   propertyName;
    private final String   className;
    private final Class<?> clazz;
    private final Method   readMethod;
    private final Method   writeMethod;
    private final int      hashCode;

    private PropertyHolder (final Builder builder) {
        this.propertyName = builder.propertyName;
        this.className = builder.className;
        this.clazz = builder.clazz;
        this.readMethod = builder.readMethod;
        this.writeMethod = builder.writeMethod;
        this.hashCode = getHashCode ();
    }

    public String getPropertyName () {
        return propertyName;
    }

    public String getClassName () {
        return className;
    }

    public Class<?> getClassType () {
        return clazz;
    }

    public Object getValue (final Object parent) {
        try {
            return readMethod.invoke (parent);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    public Object setValue (final Object parent, final Object value) {
        try {
            return writeMethod.invoke (parent, value);
        } catch (IllegalAccessException e) {
            return null;
        } catch (InvocationTargetException e) {
            return null;
        }
    }

    @Override
    public int hashCode () {
        return hashCode;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) { return true; }
        if (o == null || getClass () != o.getClass ()) { return false; }

        PropertyHolder that = (PropertyHolder) o;

        if (hashCode != that.hashCode) { return false; }
        if (className != null ? !className.equals (that.className) : that.className != null) { return false; }
        if (clazz != null ? !clazz.equals (that.clazz) : that.clazz != null) { return false; }
        if (propertyName != null ? !propertyName.equals (that.propertyName) : that.propertyName != null) {
            return false;
        }
        if (readMethod != null ? !readMethod.equals (that.readMethod) : that.readMethod != null) { return false; }
        if (writeMethod != null ? !writeMethod.equals (that.writeMethod) : that.writeMethod != null) { return false; }

        return true;
    }

    private int getHashCode () {
        int result = propertyName != null ? propertyName.hashCode () : 0;
        result = 31 * result + (className != null ? className.hashCode () : 0);
        result = 31 * result + (clazz != null ? clazz.hashCode () : 0);
        result = 31 * result + (readMethod != null ? readMethod.hashCode () : 0);
        result = 31 * result + (writeMethod != null ? writeMethod.hashCode () : 0);
        result = 31 * result + hashCode;
        return result;
    }

    public static final class Builder {
        private final String   propertyName;
        private final String   className;
        private final Class<?> clazz;
        private       Method   readMethod;
        private       Method   writeMethod;

        public Builder (final String propertyName, final Class<?> clazz) {
            this.propertyName = propertyName;
            this.clazz = clazz;
            this.className = clazz.getName ();
        }

        public Builder withReadMethod (final Method readMethod) {
            this.readMethod = readMethod;
            return this;
        }

        public Builder withWriteMethod (final Method writeMethod) {
            this.writeMethod = writeMethod;
            return this;
        }

        public PropertyHolder build () {
            return new PropertyHolder (this);
        }
    }
}
