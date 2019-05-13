package net.mrgregorix.variant.scanner.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.inject.api.injector.InjectionException;
import net.mrgregorix.variant.inject.core.VariantInjectorImpl;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import net.mrgregorix.variant.scanner.core.scenario.allsimple.Dependency1;
import net.mrgregorix.variant.scanner.core.scenario.allsimple.Dependency2;
import net.mrgregorix.variant.scanner.core.scenario.allsimple.Main;
import net.mrgregorix.variant.scanner.core.scenario.allsimple.Other;
import net.mrgregorix.variant.scanner.core.scenario.customannotation.Dependency;
import net.mrgregorix.variant.scanner.core.scenario.customannotation.MainConstructor;
import net.mrgregorix.variant.scanner.core.scenario.customannotation.MainField;
import net.mrgregorix.variant.scanner.core.scenario.cyclic.CyclicDependency1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestVariantScanner
{
    private static final Random random = new Random();

    private VariantScanner setupScanner()
    {
        return new VariantBuilder()
            .withModules(new VariantInjectorImpl(), new VariantScannerImpl())
            .build()
            .getModule(VariantScanner.class);
    }

    private static Collection<Class<?>> randomOrder(final Class<?>... classes)
    {
        final List<Class<?>> classesList = Arrays.asList(classes);
        Collections.shuffle(classesList, TestVariantScanner.random);
        return classesList;
    }

    @SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
    public static <T> T getObject(final Collection<Object> objects, final Class<T> type)
    {
        final Optional<Object> optional = objects.stream().filter(type::isInstance).findFirst();
        assertThat(type.getName() + " instance not found", optional.isPresent());
        return (T) optional.get();
    }

    private static void testAllSimple(final Collection<Object> objects)
    {
        assertThat("missing dependencies", objects, hasSize(4));

        final Main main = getObject(objects, Main.class);
        final Dependency1 dependency1 = getObject(objects, Dependency1.class);
        final Dependency2 dependency2 = getObject(objects, Dependency2.class);
        getObject(objects, Other.class);

        assertThat("missing dependency", main.getDependency1(), is(sameInstance(dependency1)));
        assertThat("missing dependency", main.getDependency2(), is(sameInstance(dependency2)));
        assertThat("missing dependency", dependency1.getDependency2(), is(sameInstance(dependency2)));
    }

    @RepeatedTest(5)
    public void testOrders()
    {
        testAllSimple(
            this.setupScanner().instantiate(randomOrder(Dependency1.class, Dependency2.class, Main.class, Other.class))
        );
    }

    @Test
    public void testPackage()
    {
        testAllSimple(
            this.setupScanner().instantiate(Main.class.getPackageName())
        );
    }

    @Test
    public void testCustomAnnotation()
    {
        final Collection<Object> objects = this.setupScanner().instantiate(MainConstructor.class.getPackageName());

        final MainConstructor mainConstructor = getObject(objects, MainConstructor.class);
        final MainField mainField = getObject(objects, MainField.class);
        final Dependency dependency = getObject(objects, Dependency.class);

        assertThat("missing dependencies", mainConstructor.getDependency(), is(sameInstance(dependency)));
        assertThat("missing dependencies", mainField.getDependency(), is(sameInstance(dependency)));
    }

    @Test
    public void testCyclic()
    {
        Assertions.assertThrows(InjectionException.class, () -> this.setupScanner().instantiate(CyclicDependency1.class.getPackageName()), "Cyclic dependencies did not threw");
    }
}
