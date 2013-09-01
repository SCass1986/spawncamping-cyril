package org.stephen.hashmap;

public final class CacheObject {
    private final String stringValue;
    private final long   longValue;
    private final double doubleValue;

    public CacheObject (final String stringValue, final long longValue, final double doubleValue) {
        this.stringValue = stringValue;
        this.longValue = longValue;
        this.doubleValue = doubleValue;
    }

    public String getStringValue () {
        return stringValue;
    }

    public long getLongValue () {
        return longValue;
    }

    public double getDoubleValue () {
        return doubleValue;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) { return true; }
        if (o == null || getClass () != o.getClass ()) { return false; }

        CacheObject that = (CacheObject) o;

        if (Double.compare (that.doubleValue, doubleValue) != 0) { return false; }
        if (longValue != that.longValue) { return false; }
        if (stringValue != null ? !stringValue.equals (that.stringValue) : that.stringValue != null) { return false; }

        return true;
    }

    @Override
    public int hashCode () {
        int result;
        long temp;
        result = stringValue != null ? stringValue.hashCode () : 0;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        temp = Double.doubleToLongBits (doubleValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}