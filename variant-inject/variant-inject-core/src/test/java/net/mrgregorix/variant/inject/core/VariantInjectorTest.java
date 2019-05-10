package net.mrgregorix.variant.inject.core;

import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.injector.SingletonInjectorAlreadyRegisteredException;
import net.mrgregorix.variant.inject.core.injector.SingletonInjector;
import net.mrgregorix.variant.inject.core.injector.SuperSingletonInjector;
import net.mrgregorix.variant.inject.core.injector.TestInjector;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VariantInjectorTest
{
    @Test
    public void testModuleRegistration() throws SingletonInjectorAlreadyRegisteredException
    {
        final VariantInjector variantInjector = new VariantInjectorImpl();
        final TestInjector injector1 = new TestInjector();
        final TestInjector injector2 = new TestInjector();
        final TestInjector singletonInjector1 = new SingletonInjector();
        final TestInjector singletonInjector2 = new SingletonInjector();

        variantInjector.registerCustomInjector(injector1);
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), hasSize(1));
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), contains(injector1));

        variantInjector.registerCustomInjector(injector2);
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), hasSize(2));
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), containsInAnyOrder(injector1, injector2));

        assertThrows(IllegalArgumentException.class, () -> variantInjector.registerCustomInjector(injector1), "duplicated injectors should not be allowed");

        variantInjector.registerCustomInjector(singletonInjector1);
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), hasSize(3));
        assertThat("injector was not registered", variantInjector.getCustomInjectors(), containsInAnyOrder(injector1, injector2, singletonInjector1));

        assertThrows(SingletonInjectorAlreadyRegisteredException.class, () -> variantInjector.registerCustomInjector(singletonInjector2), "duplicated singleton injectors should not be allowed");
    }

    @Test
    public void testSingletonGet() throws SingletonInjectorAlreadyRegisteredException
    {
        final VariantInjector variantInjector = new VariantInjectorImpl();
        final TestInjector testInjector = new TestInjector();
        final TestInjector singletonInjector = new SingletonInjector();
        final TestInjector singletonInjector2 = new SuperSingletonInjector();

        variantInjector.registerCustomInjector(testInjector);
        assertThrows(IllegalArgumentException.class, () -> variantInjector.getSingletonInjector(TestInjector.class), "non-singleton injection was returned");

        variantInjector.registerCustomInjector(singletonInjector);
        assertThat("can't get a singleton injector", variantInjector.getSingletonInjector(SingletonInjector.class), sameInstance(singletonInjector));

        variantInjector.registerCustomInjector(singletonInjector2);
        assertThat("can't get a singleton injector", variantInjector.getSingletonInjector(SuperSingletonInjector.class), sameInstance(singletonInjector2));

        assertThrows(AmbiguousException.class, () -> variantInjector.getSingletonInjector(TestInjector.class), "ambiguous query should result in AmbiguousException");
        assertThrows(AmbiguousException.class, () -> variantInjector.getSingletonInjector(SingletonInjector.class), "ambiguous query should result in AmbiguousException");
    }
}
