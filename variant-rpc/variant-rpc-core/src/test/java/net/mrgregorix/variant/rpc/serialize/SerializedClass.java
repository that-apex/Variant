package net.mrgregorix.variant.rpc.serialize;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SerializedClass extends ParentClass
{
    public static boolean      SETTER_USED = false;
    public        int          i;
    public        long         j;
    public        double       k;
    public        CustomType   x;
    public        Date         date;
    public        File         file;
    public        String       nullValue;
    public        Inner        inner;
    public        double[]     doubleArray;
    public        Object[]     customArray;
    public        List<String> hello;
    public        int[][]      multiDimensions;

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        SerializedClass that = (SerializedClass) o;
        return super.equals(o) &&
               this.i == that.i &&
               this.j == that.j &&
               Double.compare(that.k, this.k) == 0 &&
               Objects.equals(this.x, that.x) &&
               Objects.equals(this.date, that.date) &&
               Objects.equals(this.file, that.file) &&
               Objects.equals(this.nullValue, that.nullValue) &&
               Objects.equals(this.inner, that.inner) &&
               Arrays.equals(this.doubleArray, that.doubleArray) &&
               Arrays.equals(this.customArray, that.customArray) &&
               Objects.equals(this.hello, that.hello) &&
               Objects.equals(Arrays.deepToString(this.multiDimensions), Arrays.deepToString(that.multiDimensions));
    }

    public void setDoubleArray(final double[] doubleArray)
    {
        this.doubleArray = doubleArray;
        SETTER_USED = true;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), this.i, this.j, this.k, this.x, this.date, this.file, this.inner, this.doubleArray, this.customArray, this.hello, this.multiDimensions);
    }
}
