package net.mrgregorix.variant.rpc.api.service;

import java.util.Objects;

/**
 * Represents a details on how to implement a {@link RpcService}
 *
 * @param <ServiceT> type of the service class
 * @param <ImplT>    type of the implementation class.
 */
public class ServiceImplementationDetail <ServiceT extends RpcService, ImplT extends ServiceT>
{
    private final Class<ServiceT> service;
    private final ImplT           implementation;

    /**
     * Constructs a new ServiceImplementationDetail
     *
     * @param service        a {@link RpcService} to be implemented
     * @param implementation implementation to use.
     */
    public ServiceImplementationDetail(final Class<ServiceT> service, final ImplT implementation)
    {
        this.service = service;
        this.implementation = implementation;
    }

    /**
     * Returns the service to be implemented
     *
     * @return the service to be implemented
     */
    public Class<ServiceT> getService()
    {
        return this.service;
    }

    /**
     * Returns the implementation
     *
     * @return the implementation
     */
    public ImplT getImplementation()
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
