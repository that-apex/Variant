package net.mrgregorix.variant.api.module;

import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.proxy.ProxySpecification;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * <p>A base class for a Variant module.</p>
 *
 * <p>All Variant modules are required to have at least one (and preferably exactly one) class that extends
 * <code>VariantModule</code></p>
 */
public interface VariantModule
{
    /**
     * <p>Returns a name for this variant module.</p>
     *
     * <p>This name is a purely cosmetic value. It is not used in the code for any identification or comparing.
     * One name can be used by multiple modules but doing such is strongly discouraged.</p>
     *
     * <p>Even though no restrictions being present on the name it is generally a good idea to follow the following
     * naming convention:
     * <pre>MyProjectName::MyModuleNameModule</pre>
     * for example
     * <pre>Variant::ThreadingModule</pre>
     * </p>
     *
     * @return name for this variant module
     */
    String getName();

    /**
     * Returns a collection of all {@link InstantiationStrategy} that are being provided by this module.
     * <p>
     * This will be queried only once, during the module registration. It should be an immutable collection.
     *
     * @return a collection of all {@link InstantiationStrategy} that are being provided by this module.
     */
    @CollectionMayBeImmutable
    default Collection<InstantiationStrategy<?>> getInstantiationStrategies()
    {
        return Collections.emptyList();
    }

    @CollectionMayBeImmutable
    default Collection<ProxySpecification<?>> getProxySpecifications()
    {
        return Collections.emptyList();
    }
}
