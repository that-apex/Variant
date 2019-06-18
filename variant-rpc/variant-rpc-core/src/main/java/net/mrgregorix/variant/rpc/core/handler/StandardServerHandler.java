package net.mrgregorix.variant.rpc.core.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;
import net.mrgregorix.variant.rpc.api.network.provider.RpcServerHandler;
import net.mrgregorix.variant.rpc.api.network.provider.result.FailedRpcCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.SuccessfulRpcCallResult;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.utils.exception.ExceptionUtils;

/**
 * A standard {@link RpcServerHandler} implementation that uses reflection to call the methods.
 */
public class StandardServerHandler implements RpcServerHandler
{
    private final Map<Class<? extends RpcService>, RpcService> implementationData;

    /**
     * Crates a new StandardServerHandler
     *
     * @param implementationData map containing services implementation
     */
    public StandardServerHandler(final Map<Class<? extends RpcService>, RpcService> implementationData)
    {
        this.implementationData = implementationData;
    }

    @Override
    public void newConnection(final RpcConnectionData data, final Map<String, byte[]> connectionData)
    {
        // TODO: Server attachments
    }

    @Override
    public RpcServiceCallResult requestedServiceCall(final int callId, final RpcConnectionData connection, final Class<? extends RpcService> serviceType, final Method method, final Object[] arguments)
    {
        final RpcService implementation = this.implementationData.get(serviceType);

        try
        {
            final Object returnValue = ExceptionUtils.marker(() -> method.invoke(implementation, arguments));

            return new SuccessfulRpcCallResult(callId, returnValue);
        }
        catch (final InvocationTargetException e)
        {
            if (e.getTargetException() instanceof Exception)
            {
                final Exception targetException = (Exception) e.getTargetException();

                if (! ExceptionUtils.preferRealExceptions())
                {
                    StackTraceElement[] stackTrace = targetException.getStackTrace();
                    stackTrace = ExceptionUtils.getCallsAfterMarker(stackTrace);
                    stackTrace = ExceptionUtils.removeReflectCalls(stackTrace);
                    targetException.setStackTrace(stackTrace);
                }

                return new FailedRpcCallResult(callId, targetException);
            }
            else
            {
                throw new RuntimeException(e);
            }
        }
        catch (final Exception e)
        {
            return new FailedRpcCallResult(callId, e);
        }
    }
}
