package net.mrgregorix.variant.rpc.serialize;

import java.util.Objects;

@SuppressWarnings("PublicField")
public abstract class ParentClass
{
    public int parentInt;

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        final ParentClass that = (ParentClass) o;
        return this.parentInt == that.parentInt;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.parentInt);
    }
}
