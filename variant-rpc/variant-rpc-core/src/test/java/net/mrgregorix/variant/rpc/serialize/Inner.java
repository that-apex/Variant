package net.mrgregorix.variant.rpc.serialize;

import java.util.Objects;

public class Inner
{
    public           int component;
    public transient int component2;
    public           int component3;

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
        Inner inner = (Inner) o;
        return this.component == inner.component &&
               this.component3 == inner.component3;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.component, this.component3);
    }
}
