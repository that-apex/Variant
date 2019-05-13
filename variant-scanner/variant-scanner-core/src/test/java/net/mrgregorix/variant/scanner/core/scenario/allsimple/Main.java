package net.mrgregorix.variant.scanner.core.scenario.allsimple;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class Main
{
    private final Dependency1 dependency1;
    private final Dependency2 dependency2;

    @Inject
    public Main(final Dependency1 dependency1, final Dependency2 dependency2)
    {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }

    public Dependency1 getDependency1()
    {
        return this.dependency1;
    }

    public Dependency2 getDependency2()
    {
        return this.dependency2;
    }
}
