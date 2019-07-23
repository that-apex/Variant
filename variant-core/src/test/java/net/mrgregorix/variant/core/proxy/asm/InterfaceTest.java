package net.mrgregorix.variant.core.proxy.asm;

public interface InterfaceTest
{
    @SuppressWarnings("PublicField")
    class CallCount
    {
        public static int value = 0;
    }

    void doSomething();
}
