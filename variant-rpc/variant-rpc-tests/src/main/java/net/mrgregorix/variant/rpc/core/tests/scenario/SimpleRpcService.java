package net.mrgregorix.variant.rpc.core.tests.scenario;

import java.util.Date;

import net.mrgregorix.variant.rpc.api.service.RpcService;

public interface SimpleRpcService extends RpcService
{
    Date getRemoteTime();

    int addNumbers(int x, int y);

    String getResource(String name);

    int testOverloads(); // return 0

    int testOverloads(int x); // return x

    int testThrow(int x) throws IllegalArgumentException;  // throw if x < 5 otherwise return x
}
