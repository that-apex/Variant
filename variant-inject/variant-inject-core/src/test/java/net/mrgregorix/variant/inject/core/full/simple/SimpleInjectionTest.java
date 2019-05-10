package net.mrgregorix.variant.inject.core.full.simple;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.injector.SimpleSingletonCustomInjector;
import net.mrgregorix.variant.inject.core.VariantInjectorImpl;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SimpleInjectionTest
{
    @Test
    public void testSimpleInjection()
    {
        final Variant variant = new VariantBuilder()
            .withModules(new VariantInjectorImpl())
            .build();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
            variant.instantiate(TestedClass.class), "no IllegalArgumentException is thrown even though there are no suitable values for injections");

        final String data = "hello, this is test";
        final SimpleSingletonCustomInjector singletonInjector = variant.getModule(VariantInjector.class).getSingletonInjector(SimpleSingletonCustomInjector.class);
        singletonInjector.registerValue(new SomeServiceImpl(data));
        singletonInjector.registerValue(new AmbiguousServiceImpl1());

        final TestedClass testClass = variant.instantiate(TestedClass.class);
        assertThat("invalid data returned from a service", testClass.getSomeService().getData(), equalTo(data));
        assertThat("invalid data injected", testClass.getVariant(), equalTo(variant));
        assertThat("invalid data injected", testClass.getVariantInjector(), equalTo(variant.getModule(VariantInjector.class)));

        singletonInjector.registerValue(new AmbiguousServiceImpl2());

        Assertions.assertThrows(AmbiguousException.class, () ->
            variant.instantiate(TestedClass.class), "no AmbiguousException is thrown even though there was an ambiguous value");

    }
}
