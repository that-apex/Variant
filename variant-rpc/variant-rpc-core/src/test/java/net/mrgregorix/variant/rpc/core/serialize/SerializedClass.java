package net.mrgregorix.variant.rpc.core.serialize;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class SerializedClass
{
    public int        i;
    public long       j;
    public double     k;
    public CustomType x;
    public Date       date;
    public File       file;
    public Inner      inner;

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
        return this.i == that.i &&
               this.j == that.j &&
               Double.compare(that.k, this.k) == 0 &&
               Objects.equals(this.x, that.x) &&
               Objects.equals(this.date, that.date) &&
               Objects.equals(this.file, that.file) &&
               Objects.equals(this.inner, that.inner);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.i, this.j, this.k, this.x, this.date, this.file, this.inner);
    }
}
