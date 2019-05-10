package net.mrgregorix.variant.core;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;

public class DummyModule implements VariantModule
{
    @Override
    public String getName()
    {
        return "#dummy";
    }

    @Override
    public void initialize(Variant variant)
    {

    }
}
