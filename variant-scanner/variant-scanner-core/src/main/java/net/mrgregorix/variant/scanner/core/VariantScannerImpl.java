package net.mrgregorix.variant.scanner.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.injector.SimpleSingletonCustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.inject.core.injector.SimpleSingletonCustomInjectorImpl;
import net.mrgregorix.variant.scanner.api.Managed;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;
import net.mrgregorix.variant.utils.exception.DependencyException;
import net.mrgregorix.variant.utils.graph.DirectedGraph;
import net.mrgregorix.variant.utils.graph.GraphSortUtils;
import net.mrgregorix.variant.utils.graph.SetBasedDirectedGraph;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

@ThreadSafe
public class VariantScannerImpl implements VariantScanner
{
    public static final String MODULE_NAME = "Variant::Scanner::Core";

    private Variant         variant;
    private VariantInjector variantInjector;

    @Override
    public String getName()
    {
        return VariantScannerImpl.MODULE_NAME;
    }

    @Override
    public void initialize(final Variant variant)
    {
        this.variant = variant;

        try
        {
            this.variantInjector = variant.getModule(VariantInjector.class);
        }
        catch (final IllegalArgumentException e)
        {
            throw new DependencyException("Required module VariantInjector is not found. ", e);
        }
    }

    @Override
    public Collection<Object> instantiate(final Collection<Class<?>> classes)
    {
        final SimpleSingletonCustomInjector singletonInjector = this.variantInjector.getSingletonInjector(SimpleSingletonCustomInjector.class);
        final DirectedGraph<Class<?>> dependencyGraph = new SetBasedDirectedGraph<>();

        for (final Class<?> clazz : classes)
        {
            for (final InjectableElement element : this.variantInjector.getElements(clazz, InjectableElement.class))
            {
                if (! SimpleSingletonCustomInjectorImpl.isSimplyInjectable(element))
                {
                    continue;
                }

                dependencyGraph.addEdge(element.getType(), clazz);
            }
        }

        final Set<Class<?>> loadOrder = new LinkedHashSet<>(GraphSortUtils.sortTopological(dependencyGraph));
        loadOrder.addAll(classes); // add missing values
        final List<Object> createdObjects = new ArrayList<>(loadOrder.size());

        for (final Class<?> classToLoad : loadOrder)
        {
            final Object createdObject = this.variant.instantiate(classToLoad);
            singletonInjector.registerValue(createdObject);
            createdObjects.add(createdObject);
        }

        return createdObjects;
    }

    @Override
    public Collection<Object> instantiate(final String packageName)
    {
        return this.instantiate(packageName, Collections.emptyList());
    }

    @Override
    public Collection<Object> instantiate(final String packageName, final Collection<ClassLoader> classLoaders)
    {
        final Collection<Class<?>> classes = new Reflections(packageName, classLoaders, new SubTypesScanner(false))
            .getSubTypesOf(Object.class)
            .stream()
            .filter(type -> ! type.isAnnotation())
            .filter(type -> AnnotationUtils.getAnnotation(type, Managed.class) != null)
            .collect(Collectors.toList());

        return this.instantiate(classes);
    }
}
