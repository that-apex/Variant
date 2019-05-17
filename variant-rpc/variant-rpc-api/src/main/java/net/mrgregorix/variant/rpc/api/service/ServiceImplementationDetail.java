package net.mrgregorix.variant.rpc.api.service;

import java.util.Objects;

public class ServiceImplementationDetail <ServiceT extends RpcService, ImplT extends ServiceT>
{
    private final Class<ServiceT> service;
    private final Class<ImplT>    implementation;

    public ServiceImplementationDetail(final Class<ServiceT> service, final Class<ImplT> implementation)
    {
        this.service = service;
        this.implementation = implementation;
    }

    public Class<ServiceT> getService()
    {
        return this.service;
    }

    public Class<ImplT> getImplementation()
    {
        return this.implementation;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        ServiceImplementationDetail<?, ?> that = (ServiceImplementationDetail<?, ?>) o;
        return Objects.equals(this.service, that.service) &&
               Objects.equals(this.implementation, that.implementation);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.service, this.implementation);
    }

    @Override
    public String toString()
    {
        return "ServiceImplementationDetail{" +
               "service=" + this.service +
               ", implementation=" + this.implementation +
               '}';
    }
}
