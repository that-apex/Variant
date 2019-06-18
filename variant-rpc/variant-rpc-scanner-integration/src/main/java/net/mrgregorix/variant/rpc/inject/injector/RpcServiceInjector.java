package net.mrgregorix.variant.rpc.inject.injector;

import java.util.Optional;

import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.inject.annotation.InjectRpc;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

/**
 * A {@link CustomInjector} for injecting {@link RpcService}s to elements annotated with {@link InjectRpc}
 */
public class RpcServiceInjector extends AbstractModifiablePrioritizable<CustomInjector> implements CustomInjector
{
    private final VariantRpc variantRpc;

    /**
     * Create a new RpcServiceInjector
     *
     * @param variantRpc {@link VariantRpc} instance, used for getting services instances.
     */
    public RpcServiceInjector(final VariantRpc variantRpc)
    {
        this.variantRpc = variantRpc;
        this.setPriority(PriorityConstants.HIGHEST);
    }

    @Override
    public boolean isSingletonInjector()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Object> provideValueForInjection(final InjectableElement element)
    {
        final InjectRpc annotation = element.getAnnotation(InjectRpc.class);

        if (annotation == null)
        {
            return Optional.empty();
        }

        if (! RpcService.class.isAssignableFrom(element.getType()))
        {
            throw new IllegalStateException(element.getType() + " is not an RpcService");
        }

        return Optional.of(this.variantRpc.getService(annotation.value(), (Class<? extends RpcService>) element.getType()));
    }
}
