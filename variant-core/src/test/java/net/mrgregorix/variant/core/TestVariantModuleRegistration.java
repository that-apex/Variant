package net.mrgregorix.variant.core;


import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.ModuleHasNoImplementationException;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals(0, variant.getRegisteredModules().size(), "There are modules in the default Variant instance");

        Assertions.assertSame(variant.registerModule(simpleModule), simpleModule, "Returned value was invalid for registerModule");
        Assertions.assertEquals(1, variant.getRegisteredModules().size(), "A simple module was not registered successfully");
        Assertions.assertTrue(variant.getRegisteredModules().contains(simpleModule), "The registered module is not the same that is held by the Variant instance");

        Assertions.assertSame(variant.registerModule(simpleModule), simpleModule, "Returned value was invalid for registerModule");
        Assertions.assertEquals(1, variant.getRegisteredModules().size(), "The simple module was doubled");
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
    public void testAdvancedModuleRegistration() throws ClassNotFoundException
    {
        final Variant variant = this.newVariant();
        Assertions.assertEquals(0, variant.getRegisteredModules().size(), "There are modules in the default Variant instance");

        final AdvancedModule advancedModule = variant.registerModule(AdvancedModule.class);
        Assertions.assertEquals(1, variant.getRegisteredModules().size(), "An advanced module was not registered successfully");
        Assertions.assertTrue(variant.getRegisteredModules().contains(advancedModule), "An advanced module was not registered successfully");
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
        Assertions.assertEquals(variant.getRegisteredModules().size(), 0, "There are modules in the default Variant instance");

        Assertions.assertThrows(ClassNotFoundException.class, () -> variant.registerModule(InvalidModule.class), "ClassNotFoundException should be thrown for an invalid module implementation");

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
    public void testInstanceHoldingModules() throws ClassNotFoundException
    {
        final Variant variant = this.newVariant();
        final InstanceHoldingModule module = variant.registerModule(InstanceHoldingModule.class);
        Assertions.assertSame(variant, module.getVariant(), "invalid Variant instance was send to the module");
    }
}
