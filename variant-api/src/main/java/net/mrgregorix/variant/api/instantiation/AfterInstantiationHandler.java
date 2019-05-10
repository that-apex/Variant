package net.mrgregorix.variant.api.instantiation;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;

/**
 * Handler that is invoked every time a new object is created using {@link Variant#instantiate(Class)}
 */
public interface AfterInstantiationHandler extends ModifiablePrioritizable<AfterInstantiationHandler>
{
    /**
     * Invoked after a new object is created using {@link Variant#instantiate(Class)}}
     *
     * @param createdObject the newly created object
     */
    void afterInstantiationHandler(final Proxy createdObject);
}
