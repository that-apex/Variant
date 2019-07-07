package net.mrgregorix.variant.rpc.core.tests.scenario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.FailedAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.SuccessAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;
import net.mrgregorix.variant.rpc.core.authenticator.AbstractIdentifiableAuthenticator;

public class CustomAuthenticator extends AbstractIdentifiableAuthenticator
{
    private static final Random random = new Random();

    public CustomAuthenticator()
    {
        super(CustomAuthenticator.class.getName());
    }

    @Override
    protected void clientConnected0(final RpcConnectionData connection, final DataOutputStream data) throws IOException
    {
        final byte value1 = (byte) random.nextInt(10);
        final byte value2 = (byte) random.nextInt(10);

        connection.getAuthData().put(CustomAuthenticator.class.getName() + ".expected_value", value1 * value2);

        data.write(value1);
        data.write(value2);
    }

    @Override
    public AuthenticationResult dataReceivedFromClient(final RpcConnectionData connection, final DataInputStream data) throws IOException
    {
        if (data.readByte() != ((Number) connection.getAuthData().get(CustomAuthenticator.class.getName() + ".expected_value")).byteValue())
        {
            return new FailedAuthenticationResult("Invalid result");
        }

        return SuccessAuthenticationResult.INSTANCE;
    }

    @Override
    public void dataReceivedFromServer(final DataInputStream input, final DataOutputStream output) throws IOException
    {
        output.writeByte(input.readByte() * input.readByte());
    }
}
