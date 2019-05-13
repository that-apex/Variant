package net.mrgregorix.variant.scanner.core.scenario.cyclic;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class CyclicDependency2
{
    private final CyclicDependency1 cyclicDependency1;

    @Inject
    public CyclicDependency2(final CyclicDependency1 cyclicDependency1)
    {
        this.cyclicDependency1 = cyclicDependency1;
    }

    public CyclicDependency1 getCyclicDependency1()
    {
        return this.cyclicDependency1;
    }
}
