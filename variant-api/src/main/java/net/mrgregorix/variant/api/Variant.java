package net.mrgregorix.variant.api;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.module.ModuleHasNoImplementationException;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.api.proxy.ProxyCache;
import net.mrgregorix.variant.api.proxy.ProxyProvider;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * <p>Core class for Variant framework</p>
 *
 * <p>
 * It provides a core functionalities for all variant applications, like registering submodules to use and creating Variant-managed classes.
 * </p>
 */
@ThreadSafe
public interface Variant
{
    /**
     * <p>Create a new instance of a Variant-managed class.</p>
     *
     * <p>If the <code>type</code> is used for the first time in this
     * <code>Variant</code> instance, a new proxy class will be created.
     * Any method called from a Variant-managed class can be intercepted by any of the registered Variant modules</p>
     *
     * <p>This method never returns an instance with the exact type as
     * specified by <code>type</code> parameter, rather it returns an object that is an instance of a proxy class generated in runtime that extends the given type. Therefore then given type CAN NOT be
     * a final class nor have final methods.</p>
     *
     * @param type type of the class to be instantiated
     * @param <T>  type of the class to be instantiated
     *
     * @return a newly instantiated object
     */
    <T> T instantiate(Class<T> type) throws VariantInstantiationException;

    /**
     * Returns a collection containing all the {@link VariantModule}s that were registered to this Variant instance.
     *
     * @return registered {@link VariantModule}s
     */
    @CollectionMayBeImmutable
    Collection<VariantModule> getRegisteredModules();

    /**
     * Registers a new module that will be used for all future instantiations.
     * <p>
     * Registering a module does not affect any object instances previously created by this {@link Variant} instance.
     * <p>
     * If a module is already registered nothing will happen.
     *
     * @param module an instance of the module to be registered
     * @param <T>    type of the module to be registered
     *
     * @return the exact same value that was passed in the {@code module} parameter
     */
    <T extends VariantModule> T registerModule(final T module);

    /**
     * Registers a new module that will be used for all future instantiations.
     * <p>
     * Registering a module does not affect any object instances previously created by this {@link Variant} instance.
     * <p>
     * The {@code moduleClass} must be an instantiatable class or an interface/abstract class with {@link ModuleImplementation} annotation. If such annotation is present, it will be used even if the
     * {@code moduleClass} itself is instantiable.
     * <p>
     * The module's class must have an empty constructor OR a constructor that takes only one parameter of type {@link Variant} which will be supplied by this Variant instance.
     *
     * @param moduleClass a class of the module to be registered
     * @param <T>         type of the module to be registered
     *
     * @return a newly created instance of {@code moduleClass} or a subclass specified by {@link ModuleImplementation}.
     *
     * @throws ModuleHasNoImplementationException if the provided class is not instantiable or no {@link ModuleImplementation} annotation is present.
     * @throws ClassNotFoundException             if the provided {@link ModuleImplementation} class is invalid.
     */
    <T extends VariantModule> T registerModule(final Class<T> moduleClass)
        throws ModuleHasNoImplementationException, ClassNotFoundException;

    /**
     * <p>Returns a {@link ProxyCache} that is used by this Variant instance</p>
     *
     * @return used {@link ProxyCache}
     *
     * @see ProxyCache
     */
    ProxyCache getProxyCache();

    /**
     * <p>Returns a {@link ProxyProvider} that is used by this Variant instance</p>
     *
     * @return used {@link ProxyProvider}
     *
     * @see ProxyProvider
     */
    ProxyProvider getProxyProvider();
}