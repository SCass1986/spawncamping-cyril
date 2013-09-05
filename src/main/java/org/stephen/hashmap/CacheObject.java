package org.stephen.hashmap;

public final class CacheObject {
    private String stringValue_01;
    private String stringValue_02;
    private String stringValue_03;
    private String stringValue_04;

    private long longValue_01;
    private long longValue_02;
    private long longValue_03;
    private long longValue_04;

    private double doubleValue_01;
    private double doubleValue_02;
    private double doubleValue_03;
    private double doubleValue_04;

    private final int hashCode;

    public CacheObject (final String stringValue, final long longValue, final double doubleValue) {
        this.stringValue_01 = this.stringValue_02 = this.stringValue_03 = this.stringValue_04 = stringValue;
        this.longValue_01 = this.longValue_02 = this.longValue_03 = this.longValue_04 = longValue;
        this.doubleValue_01 = this.doubleValue_02 = this.doubleValue_03 = this.doubleValue_04 = doubleValue;
        this.hashCode = getHashCode ();
    }

    public String getStringValue_01 () {
        return stringValue_01;
    }

    public String getStringValue_02 () {
        return stringValue_02;
    }

    public String getStringValue_03 () {
        return stringValue_03;
    }

    public String getStringValue_04 () {
        return stringValue_04;
    }

    public long getLongValue_01 () {
        return longValue_01;
    }

    public long getLongValue_02 () {
        return longValue_02;
    }

    public long getLongValue_03 () {
        return longValue_03;
    }

    public long getLongValue_04 () {
        return longValue_04;
    }

    public double getDoubleValue_01 () {
        return doubleValue_01;
    }

    public double getDoubleValue_02 () {
        return doubleValue_02;
    }

    public double getDoubleValue_03 () {
        return doubleValue_03;
    }

    public double getDoubleValue_04 () {
        return doubleValue_04;
    }

    public void setStringValue_01 (final String stringValue_01) {
        this.stringValue_01 = stringValue_01;
    }

    public void setStringValue_02 (final String stringValue_02) {
        this.stringValue_02 = stringValue_02;
    }

    public void setStringValue_03 (final String stringValue_03) {
        this.stringValue_03 = stringValue_03;
    }

    public void setStringValue_04 (final String stringValue_04) {
        this.stringValue_04 = stringValue_04;
    }

    public void setLongValue_01 (final long longValue_01) {
        this.longValue_01 = longValue_01;
    }

    public void setLongValue_02 (final long longValue_02) {
        this.longValue_02 = longValue_02;
    }

    public void setLongValue_03 (final long longValue_03) {
        this.longValue_03 = longValue_03;
    }

    public void setLongValue_04 (final long longValue_04) {
        this.longValue_04 = longValue_04;
    }

    public void setDoubleValue_01 (final double doubleValue_01) {
        this.doubleValue_01 = doubleValue_01;
    }

    public void setDoubleValue_02 (final double doubleValue_02) {
        this.doubleValue_02 = doubleValue_02;
    }

    public void setDoubleValue_03 (final double doubleValue_03) {
        this.doubleValue_03 = doubleValue_03;
    }

    public void setDoubleValue_04 (final double doubleValue_04) {
        this.doubleValue_04 = doubleValue_04;
    }

    @Override
    public int hashCode () {
        return hashCode;
    }

    private int getHashCode () {
        int result;
        long temp;
        result = stringValue_01 != null ? stringValue_01.hashCode () : 0;
        result = 31 * result + (stringValue_02 != null ? stringValue_02.hashCode () : 0);
        result = 31 * result + (stringValue_03 != null ? stringValue_03.hashCode () : 0);
        result = 31 * result + (stringValue_04 != null ? stringValue_04.hashCode () : 0);
        result = 31 * result + (int) (longValue_01 ^ (longValue_01 >>> 32));
        result = 31 * result + (int) (longValue_02 ^ (longValue_02 >>> 32));
        result = 31 * result + (int) (longValue_03 ^ (longValue_03 >>> 32));
        result = 31 * result + (int) (longValue_04 ^ (longValue_04 >>> 32));
        temp = Double.doubleToLongBits (doubleValue_01);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits (doubleValue_02);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits (doubleValue_03);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits (doubleValue_04);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) { return true; }
        if (o == null || getClass () != o.getClass ()) { return false; }

        CacheObject that = (CacheObject) o;

        if (Double.compare (that.doubleValue_01, doubleValue_01) != 0) { return false; }
        if (Double.compare (that.doubleValue_02, doubleValue_02) != 0) { return false; }
        if (Double.compare (that.doubleValue_03, doubleValue_03) != 0) { return false; }
        if (Double.compare (that.doubleValue_04, doubleValue_04) != 0) { return false; }
        if (longValue_01 != that.longValue_01) { return false; }
        if (longValue_02 != that.longValue_02) { return false; }
        if (longValue_03 != that.longValue_03) { return false; }
        if (longValue_04 != that.longValue_04) { return false; }
        if (stringValue_01 != null ? !stringValue_01.equals (that.stringValue_01) : that.stringValue_01 != null) {
            return false;
        }
        if (stringValue_02 != null ? !stringValue_02.equals (that.stringValue_02) : that.stringValue_02 != null) {
            return false;
        }
        if (stringValue_03 != null ? !stringValue_03.equals (that.stringValue_03) : that.stringValue_03 != null) {
            return false;
        }
        if (stringValue_04 != null ? !stringValue_04.equals (that.stringValue_04) : that.stringValue_04 != null) {
            return false;
        }

        return true;
    }
}