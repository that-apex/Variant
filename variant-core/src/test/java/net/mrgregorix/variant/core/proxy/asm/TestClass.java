package net.mrgregorix.variant.core.proxy.asm;

import net.mrgregorix.variant.api.annotations.NoProxy;

public class TestClass
{
    private int x = 0;

    public void setX(int x)
    {
        this.x = x;
    }

    @NoProxy
    public int getX()
    {
        return this.x;
    }
}
