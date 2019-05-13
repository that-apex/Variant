package net.mrgregorix.variant.scanner.core.scenario.allsimple;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class Dependency1
{
    @Inject
    private Dependency2 dependency2;

    public Dependency2 getDependency2()
    {
        return this.dependency2;
    }
}
