package net.mrgregorix.variant.rpc.core.scenario;

import java.util.Date;

import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.base.RpcServiceName;

@RpcServiceName("simple-rpc-service")
public interface SimpleRpcService extends RpcService
{
    Date getRemoteTime();

    int addNumbers(int x, int y);

    String getResource(String name);
}
