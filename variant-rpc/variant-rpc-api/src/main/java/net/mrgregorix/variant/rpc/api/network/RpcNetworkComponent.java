package net.mrgregorix.variant.rpc.api.network;

public interface RpcNetworkComponent
{
    String getName();

    String getAddress();

    int getPort();

    boolean isRunning();

    void start();

    void startBlocking();

    void stop();

    void stopBlocking();
}
