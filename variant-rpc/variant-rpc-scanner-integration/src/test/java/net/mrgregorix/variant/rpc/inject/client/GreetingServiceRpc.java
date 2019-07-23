package net.mrgregorix.variant.rpc.inject.client;

import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceInfo;

@RpcServiceInfo(groups = "test-client")
public interface GreetingServiceRpc extends RpcService
{
    void greet(String name);
}
