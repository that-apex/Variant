package net.mrgregorix.variant.rpc.api.network;

/**
 * Represents an RPC network component.
 * <p>
 * This should always be either {@link RpcNetworkClient} or {@link RpcNetworkServer}
 */
public interface RpcNetworkComponent
{
    /**
     * Returns a unique name of this component.
     *
     * @return a unique name of this component.
     */
    String getName();

    /**
     * Returns an address that this component uses
     *
     * @return an address that this component uses
     */
    String getAddress();

    /**
     * Returns the port that this component uses
     *
     * @return the port that this component uses
     */
    int getPort();

    /**
     * Returns whether this component is running
     *
     * @return whether this component is running
     */
    boolean isRunning();

    /**
     * Starts this component asynchronously.
     */
    void start();

    /**
     * Starts this component synchronously.
     */
    void startBlocking();

    /**
     * Stops this component asynchronously.
     */
    void stop();

    /**
     * Starts this component synchronously.
     */
    void stopBlocking();
}
