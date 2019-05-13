package net.mrgregorix.variant.scanner.core.scenario.cyclic;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class CyclicDependency1
{
    private final CyclicDependency2 cyclicDependency2;

    @Inject
    public CyclicDependency1(final CyclicDependency2 cyclicDependency2)
    {
        this.cyclicDependency2 = cyclicDependency2;
    }

    public CyclicDependency2 getCyclicDependency2()
    {
        return this.cyclicDependency2;
    }
}
