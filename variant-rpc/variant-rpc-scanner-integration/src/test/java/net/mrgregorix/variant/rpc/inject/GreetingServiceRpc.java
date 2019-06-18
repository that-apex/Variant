package net.mrgregorix.variant.rpc.inject;

import net.mrgregorix.variant.rpc.api.service.RpcService;

public interface GreetingServiceRpc extends RpcService
{
    void greet(String name);
}
