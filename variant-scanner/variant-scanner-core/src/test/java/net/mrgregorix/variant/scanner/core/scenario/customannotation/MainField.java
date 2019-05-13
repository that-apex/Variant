package net.mrgregorix.variant.scanner.core.scenario.customannotation;

@CustomManaged
public class MainField
{
    @CustomSimpleInject
    private Dependency dependency;

    public Dependency getDependency()
    {
        return this.dependency;
    }
}
