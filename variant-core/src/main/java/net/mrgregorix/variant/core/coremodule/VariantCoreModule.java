package net.mrgregorix.variant.core.coremodule;

import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.module.VariantModule;

/**
 * A basic module that supports basic functionalities for Variant.
 */
public class VariantCoreModule implements VariantModule
{
    public static final String NAME = "Variant::Core";

    @Override
    public String getName()
    {
        return VariantCoreModule.NAME;
    }

    @Override
    public Collection<InstantiationStrategy<?>> getInstantiationStrategies()
    {
        return Collections.singletonList(new EmptyConstructorInstantiationStrategy());
    }
}
