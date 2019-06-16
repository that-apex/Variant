package net.mrgregorix.variant.core.proxy.asm;


import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.core.ProxyEverythingModule;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AsmProxyProviderTest
{
    private Variant variant;

    @BeforeEach
    public void init()
    {
        this.variant = new VariantBuilder()
            .withProxyProvider(new AsmProxyProvider())
            .build();
    }

    @Test
    public void test()
    {
        assertThat("proxy provider was not set", this.variant.getProxyProvider(), is(instanceOf(AsmProxyProvider.class)));

        this.variant.registerModule(new ProxyEverythingModule(new TestHandler()));

        final TestClass instance = this.variant.instantiate(TestClass.class);
        assertThat("instance is null", instance, is(notNullValue()));
        assertThat("instance is not a proxy", instance, is(instanceOf(Proxy.class)));
        assertThat("instance base class is invalid", ((Proxy) instance).getProxyBaseClass(), is(TestClass.class));
        instance.setX(10);
        assertThat("proxy does not work", instance.getX(), is(15));

        final TestClass instance2 = this.variant.instantiate(TestClass.class);
        instance2.setX(21);
        assertThat("instance is null", instance2, is(notNullValue()));
        assertThat("instance is not a proxy", instance2, is(instanceOf(Proxy.class)));
        assertThat("proxy does not work", instance2.getX(), is(26));
        assertThat("created classes are not the same", instance.getClass(), sameInstance(instance2.getClass()));
    }

    @Test
    public void testCallWithoutProxy()
    {
        final MarkingTestHandler handler = new MarkingTestHandler();
        this.variant.registerModule(new ProxyEverythingModule(handler));

        final MarkingTestClass instance = this.variant.instantiate(MarkingTestClass.class);
        assertThat("instance base class is invalid", ((Proxy) instance).getProxyBaseClass(), is(MarkingTestClass.class));

        instance.method();
        assertThat("method was not called", instance.getAndClearFlag());
        assertThat("handler was not called", handler.getAndClearFlag());

        assertThat("flag was not cleared", not(instance.getAndClearFlag()));
        assertThat("flag was not cleared", not(handler.getAndClearFlag()));

        ((Proxy) instance).runWithoutProxy(instance::method);
        assertThat("method was not called", instance.getAndClearFlag());
        assertThat("handler was not called", not(handler.getAndClearFlag()));

        instance.method();
        assertThat("method was not called", instance.getAndClearFlag());
        assertThat("handler was not called", handler.getAndClearFlag());

        final String value = ((Proxy) instance).callWithoutProxy(instance::method);
        assertThat("method was not called", instance.getAndClearFlag());
        assertThat("handler was not called", not(handler.getAndClearFlag()));
        assertThat(value, equalTo("hello"));
    }

    @Test
    public void testInterface()
    {
        this.variant.registerModule(new ProxyEverythingModule(new InterfaceTestHandler()));

        final InterfaceTest instance = this.variant.instantiate(InterfaceTest.class);
        assertThat("instance base class is invalid", ((Proxy) instance).getProxyBaseClass(), is(InterfaceTest.class));

        final int callCount = InterfaceTest.CallCount.VALUE;
        instance.doSomething();
        assertThat("method was not called", callCount + 1, is(InterfaceTest.CallCount.VALUE));
    }
}
