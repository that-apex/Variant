package net.mrgregorix.variant.core;


import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.ModuleHasNoImplementationException;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestVariantModuleRegistration
{
    private Variant newVariant()
    {
        return new VariantBuilder().noDefaultModule().build();
    }

    public static class SimpleModule extends DummyModule
    {
    }

    @Test
    public void testBaseModuleRegistration()
    {
        final Variant variant = this.newVariant();
        final SimpleModule simpleModule = new SimpleModule();

        assertThat("There are modules in the default Variant instance", variant.getRegisteredModules(), hasSize(0));
        assertThat("Returned value was invalid for registerModule", variant.registerModule(simpleModule), sameInstance(simpleModule));
        assertThat("A simple module was not registered successfully", variant.getRegisteredModules(), hasSize(1));
        assertThat("The registered module is not the same that is held by the Variant instance", variant.getRegisteredModules(), contains(simpleModule));
        assertThat("Returned value was invalid for registerModule", variant.registerModule(simpleModule), sameInstance(simpleModule));
        assertThat("The simple module was doubled", variant.getRegisteredModules(), hasSize(1));
    }

    @ModuleImplementation("net.mrgregorix.variant.core.TestVariantModuleRegistration$AdvancedModuleImpl")
    public interface AdvancedModule extends VariantModule
    {
    }

    public static class AdvancedModuleImpl extends DummyModule implements AdvancedModule
    {
        public AdvancedModuleImpl()
        {
        }
    }

    @Test
    public void testAdvancedModuleRegistration()
    {
        final Variant variant = this.newVariant();
        assertThat("There are modules in the default Variant instance", variant.getRegisteredModules(), hasSize(0));

        final AdvancedModule advancedModule = variant.registerModule(AdvancedModule.class);
        assertThat("An advanced module was not registered successfully", variant.getRegisteredModules(), hasSize(1));
        assertThat("An advanced module was not registered successfully", variant.getRegisteredModules(), contains(advancedModule));
    }

    @Test
    public void testGetModule()
    {
        final Variant variant = this.newVariant();

        Assertions.assertThrows(IllegalArgumentException.class, () -> variant.getModule(SimpleModule.class), "No exception thrown when querying for an invalid module");

        final SimpleModule simpleModule = variant.registerModule(new SimpleModule());
        assertThat("no SimpleModule was found even though it was registered", variant.getModule(SimpleModule.class), sameInstance(simpleModule));
        assertThat("no SimpleModule was found even though it was registered", variant.getModule(DummyModule.class), sameInstance(simpleModule));

        final AdvancedModule advancedModule = variant.registerModule(AdvancedModule.class);
        assertThat("no SimpleModule was found even though it was registered", variant.getModule(SimpleModule.class), sameInstance(simpleModule));
        assertThat("no AdvancedModule was found even though it was registered", variant.getModule(AdvancedModule.class), sameInstance(advancedModule));

        Assertions.assertThrows(AmbiguousException.class, () -> variant.getModule(DummyModule.class), "No exception thrown when querying for an ambiguous module");
    }

    @ModuleImplementation("hello.i.dont.exist")
    public interface InvalidModule extends VariantModule
    {
    }

    public static class InaccessibleModule extends DummyModule implements VariantModule
    {
        private InaccessibleModule()
        {
        }
    }

    @Test
    public void testInvalidModules()
    {
        final Variant variant = this.newVariant();
        assertThat("There are modules in the default Variant instance", variant.getRegisteredModules(), hasSize(0));

        Assertions.assertThrows(
            ModuleHasNoImplementationException.class, () -> variant.registerModule(InvalidModule.class), "ClassNotFoundException should be thrown for an invalid module implementation");

        Assertions.assertThrows(
            ModuleHasNoImplementationException.class, () -> variant.registerModule(InaccessibleModule.class),
            "ModuleHasNoImplementationException should be thrown for an implementation that has no visible constructor"
        );
    }

    public static class InstanceHoldingModule extends DummyModule implements VariantModule
    {
        private final Variant variant;

        public InstanceHoldingModule(final Variant variant)
        {
            this.variant = variant;
        }

        public Variant getVariant()
        {
            return this.variant;
        }
    }

    @Test
    public void testInstanceHoldingModules()
    {
        final Variant variant = this.newVariant();
        final InstanceHoldingModule module = variant.registerModule(InstanceHoldingModule.class);
        assertThat("invalid Variant instance was send to the module", module.getVariant(), sameInstance(variant));
    }
}
