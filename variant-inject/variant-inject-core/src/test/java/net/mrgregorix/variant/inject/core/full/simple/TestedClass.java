package net.mrgregorix.variant.inject.core.full.simple;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.annotation.Inject;

public class TestedClass
{
    private final SomeService someService;
    private final Variant     variant;

    @Inject
    private VariantInjector variantInjector;

    @Inject
    private AmbiguousService ambiguousService;

    @Inject
    public TestedClass(final SomeService someService, final Variant variant)
    {
        this.someService = someService;
        this.variant = variant;
    }

    public SomeService getSomeService()
    {
        return this.someService;
    }

    public Variant getVariant()
    {
        return this.variant;
    }

    public VariantInjector getVariantInjector()
    {
        return this.variantInjector;
    }
}