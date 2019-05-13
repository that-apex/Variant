package net.mrgregorix.variant.scanner.api;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.annotation.info.InjectableSingleton;

/**
 * The core class for the Variant Scanner module. It allows to instantiate classes in bulk, while maintaining the correct load order.
 * <p>
 * It also allows for scanning for all classes in the given package.
 */
@ThreadSafe
@InjectableSingleton
@ModuleImplementation("net.mrgregorix.variant.scanner.core.VariantScannerImpl")
public interface VariantScanner extends VariantModule
{
    /**
     * Instantiates all the given classes using {@link Variant#instantiate(Class)} in a correct order.
     * <p>
     * It will ensure that if class A depends on class B via a simple injection (see {@link Inject#simpleInjection()}), B will be loaded before A.
     * <p>
     * All the instantiated classes will be available for simple injection.
     *
     * @param classes classes to be loaded
     *
     * @return collection of all created objects
     */
    Collection<Object> instantiate(Collection<Class<?>> classes);

    /**
     * Instantiates all the classes in the given package annotated by {@link Managed} using {@link Variant#instantiate(Class)} in a correct order.
     * <p>
     * It will ensure that if class A depends on class B via a simple injection (see {@link Inject#simpleInjection()}), B will be loaded before A.
     * <p>
     * All the instantiated classes will be available for simple injection
     *
     * @param packageName name of the package to be scanned
     *
     * @return collection of all created objects
     */
    Collection<Object> instantiate(String packageName);

    /**
     * Instantiates all the classes in the given package annotated by {@link Managed} using {@link Variant#instantiate(Class)} in a correct order.
     * <p>
     * It will ensure that if class A depends on class B via a simple injection (see {@link Inject#simpleInjection()}), B will be loaded before A.
     * <p>
     * All the instantiated classes will be available for simple injection
     *
     * @param packageName  name of the package to be scanned
     * @param classLoaders class loaders that will be used for finding the classes
     *
     * @return collection of all created objects
     */
    Collection<Object> instantiate(String packageName, Collection<ClassLoader> classLoaders);
}
