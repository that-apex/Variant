package net.mrgregorix.variant.rpc.serialize;

import java.util.Objects;

public class CustomType
{
    public int x;
    public int y;
    public int sum;

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
        CustomType that = (CustomType) o;
        return this.sum == that.sum || (this.x + this.y == that.sum) || (that.x + that.y == this.sum);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.sum);
    }
}
