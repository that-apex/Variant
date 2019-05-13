package net.mrgregorix.variant.scanner.core.scenario.customannotation;

@CustomManaged
public class MainConstructor
{
    private final Dependency dependency;

    @CustomSimpleInject
    public MainConstructor(final Dependency dependency)
    {
        this.dependency = dependency;
    }

    public Dependency getDependency()
    {
        return this.dependency;
    }
}
