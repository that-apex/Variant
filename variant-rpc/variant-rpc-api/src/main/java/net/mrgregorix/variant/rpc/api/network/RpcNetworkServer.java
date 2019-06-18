package net.mrgregorix.variant.rpc.api.network;

import java.util.Collection;

import net.mrgregorix.variant.rpc.api.network.provider.RpcNetworkingProvider;
import net.mrgregorix.variant.rpc.api.network.provider.RpcServerHandler;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;

/**
 * Represents an RPC server.
 *
 * @see RpcNetworkingProvider#setupServer(String, String, int, Collection, RpcServerHandler, DataSerializer, DataSerializer)
 */
public interface RpcNetworkServer extends RpcNetworkComponent
{
}
