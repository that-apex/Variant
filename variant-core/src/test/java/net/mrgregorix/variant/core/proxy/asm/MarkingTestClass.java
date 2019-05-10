package net.mrgregorix.variant.core.proxy.asm;

import net.mrgregorix.variant.api.annotations.NoProxy;

public class MarkingTestClass
{
    private boolean flag;

    public String method()
    {
        this.flag = true;
        return "hello";
    }

    @NoProxy
    public boolean getAndClearFlag()
    {
        if (this.flag)
        {
            this.flag = false;
            return true;
        }

        return false;
    }
}
