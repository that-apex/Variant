package net.mrgregorix.variant.rpc.core.authenticator;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.DataAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;

/**
 * An {@link RpcAuthenticator} that is able to always identify itself by a unique string identifier.
 */
public abstract class AbstractIdentifiableAuthenticator implements RpcAuthenticator
{
    /**
     * Magic to make sure we are 100% dealing with another {@link AbstractIdentifiableAuthenticator}
     */
    private static final byte[] MAGIC = new byte[] {0x72, (byte) 0xDD, (byte) 0x8A, (byte) 0xFE};

    private final String name;

    /**
     * Creates a new AbstractIdentifiableAuthenticator
     *
     * @param name a unique identifier for this authenticator TYPE (not instance)
     */
    protected AbstractIdentifiableAuthenticator(final String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public AuthenticationResult clientConnected(final RpcConnectionData connection)
    {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        try
        {
            final DataOutputStream dataOutputStream = new DataOutputStream(data);
            dataOutputStream.write(MAGIC);
            dataOutputStream.writeUTF(this.name);

            this.clientConnected0(connection, dataOutputStream);
        }
        catch (final IOException e)
        {
            throw new AssertionError("This will never happen");
        }

        return new DataAuthenticationResult(data.toByteArray());
    }

    /**
     * Called when a client has connected
     *
     * @param connection client that has connected
     * @param data       additional data to send to the client
     *
     * @throws IOException implementation dependent
     */
    protected abstract void clientConnected0(final RpcConnectionData connection, final DataOutputStream data) throws IOException;

    @Override
    public boolean matchDataReceivedFromServer(final DataInputStream input) throws IOException
    {
        final byte[] magic = new byte[4];

        if (input.available() < 4)
        {
            return false;
        }
        if (input.read(magic) != 4 || ! Arrays.equals(MAGIC, magic))
        {
            return false;
        }

        return input.readUTF().equals(this.name);
    }
}
