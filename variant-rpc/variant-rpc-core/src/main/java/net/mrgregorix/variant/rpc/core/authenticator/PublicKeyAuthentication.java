package net.mrgregorix.variant.rpc.core.authenticator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.DataAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.FailedAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.SuccessAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.io.ByteArrayUtils;

/**
 * A {@link RpcAuthenticator} that used public-key cryptography to authenticate clients.
 *
 * @param <PublicKeyT>  type of the public key class
 * @param <PrivateKeyT> type of the private key class
 */
public abstract class PublicKeyAuthentication <PublicKeyT extends PublicKey, PrivateKeyT extends PrivateKey> extends AbstractIdentifiableAuthenticator
{
    /**
     * Size of the nonce to generate
     */
    protected static final int NONCE_SIZE = 32;

    /**
     * Random used for generating nonces and other cryptographic functions
     */
    protected static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final int SUBPACKET_SERVER_TO_CLIENT_INIT          = 1;
    private static final int SUBPACKET_SERVER_TO_CLIENT_NONCE         = 2;
    private static final int SUBPACKET_CLIENT_TO_SERVER_REQUEST_NONCE = 1;
    private static final int SUBPACKET_CLIENT_TO_SERVER_SIGNED_NONCE  = 2;

    private static final String CONNECTION_DATA_KEY_NONCE = PublicKeyAuthentication.class.getName() + ".nonce";

    private final           PublicKeyT  publicKey;
    private final @Nullable PrivateKeyT privateKey;
    private final           byte[]      fingerprint;

    /**
     * Creates a new PublicKeyAuthentication, private key is required only for clients
     *
     * @param publicKey  public key
     * @param privateKey private key
     */
    public PublicKeyAuthentication(final PublicKeyT publicKey, @Nullable final PrivateKeyT privateKey)
    {
        super(PublicKeyAuthentication.class.getName());
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.fingerprint = this.computeFingerprint();
    }

    /**
     * Creates a new PublicKeyAuthentication with no private key, this can be only used as a server {@link RpcAuthenticator}
     *
     * @param publicKey public key
     */
    public PublicKeyAuthentication(final PublicKeyT publicKey)
    {
        this(publicKey, null);
    }

    /**
     * Returns the public key used by this authentication.
     *
     * @return the public key used by this authentication
     */
    public PublicKeyT getPublicKey()
    {
        return this.publicKey;
    }

    /**
     * Returns the public key used by this authentication or {@code null} if none.
     *
     * @return the public key used by this authentication or null
     */
    @Nullable
    public PrivateKeyT getPrivateKey()
    {
        return this.privateKey;
    }

    /**
     * Computes the fingerprint for the public key (see {@link #getPublicKey()})
     *
     * @return computed fingerprint
     */
    protected abstract byte[] computeFingerprint();

    /**
     * Signs the given nonce using the private key (see {@link #getPrivateKey()})
     *
     * @param nonce nonce to be signed
     *
     * @return generated signature
     */
    protected abstract byte[] signNonce(byte[] nonce);

    /**
     * Checks whether the given nonce can be verified using the public key (see {@link #getPublicKey()})
     *
     * @param actualNonce the actual nonce
     * @param signature   the signature that was received from the client
     *
     * @return whether or not the signature is valid for the given nonce
     */
    protected abstract boolean validateNonce(byte[] actualNonce, byte[] signature);

    /**
     * Generates a new, secure nonce of size {@link #NONCE_SIZE}
     *
     * @return a newly generated nonce.
     */
    protected byte[] generateNonce()
    {
        final byte[] nonce = new byte[NONCE_SIZE];
        SECURE_RANDOM.nextBytes(nonce);
        return nonce;
    }

    @Override
    protected void clientConnected0(final RpcConnectionData connection, final DataOutputStream data) throws IOException
    {
        data.writeShort(this.fingerprint.length);
        data.write(this.fingerprint);
        data.write(SUBPACKET_SERVER_TO_CLIENT_INIT); // to actually trigger #dataReceivedFromServer
    }

    @Override
    public boolean matchDataReceivedFromServer(final DataInputStream input) throws IOException
    {
        if (! super.matchDataReceivedFromServer(input))
        {
            return false;
        }

        final byte[] fingerprint = new byte[input.readUnsignedShort()];
        if (input.read(fingerprint) != fingerprint.length)
        {
            return false;
        }

        return Arrays.equals(this.fingerprint, fingerprint);
    }

    @Override
    public AuthenticationResult dataReceivedFromClient(final RpcConnectionData connection, final DataInputStream data) throws IOException
    {
        switch (data.readByte())
        {
            case SUBPACKET_CLIENT_TO_SERVER_REQUEST_NONCE:
            {
                if (connection.getAuthData().containsKey(CONNECTION_DATA_KEY_NONCE))
                {
                    return new FailedAuthenticationResult("Nonce already sent");
                }

                final byte[] nonce = this.generateNonce();
                connection.getAuthData().put(CONNECTION_DATA_KEY_NONCE, nonce);

                final byte[] response = ByteArrayUtils.prepareDataByteArray(d -> {
                    d.write(SUBPACKET_SERVER_TO_CLIENT_NONCE);
                    d.writeShort(nonce.length);
                    d.write(nonce);
                });

                return new DataAuthenticationResult(response);
            }
            case SUBPACKET_CLIENT_TO_SERVER_SIGNED_NONCE:
            {
                final byte[] nonce = (byte[]) connection.getAuthData().get(CONNECTION_DATA_KEY_NONCE);
                if (nonce == null)
                {
                    return new FailedAuthenticationResult("No nonce was ever requested from this connection");
                }
                final byte[] signature = new byte[data.readShort()];

                if (data.read(signature) != signature.length)
                {
                    return new FailedAuthenticationResult("Signature not complete");
                }

                if (! this.validateNonce(nonce, signature))
                {
                    return new FailedAuthenticationResult("Signature invalid");
                }

                return new SuccessAuthenticationResult();
            }
            default:
            {
                return new FailedAuthenticationResult("Invalid packet received");
            }
        }
    }

    @Override
    public void dataReceivedFromServer(final DataInputStream input, final DataOutputStream output) throws IOException
    {
        switch (input.readByte())
        {
            case SUBPACKET_SERVER_TO_CLIENT_INIT:
            {
                output.write(SUBPACKET_CLIENT_TO_SERVER_REQUEST_NONCE);
                break;
            }
            case SUBPACKET_SERVER_TO_CLIENT_NONCE:
            {
                final byte[] nonce = new byte[input.readShort()];
                if (input.read(nonce) != nonce.length)
                {
                    throw new IllegalStateException("Nonce not received");
                }

                final byte[] signed = this.signNonce(nonce);
                output.write(SUBPACKET_CLIENT_TO_SERVER_SIGNED_NONCE);
                output.writeShort(signed.length);
                output.write(signed);
                break;
            }
            default:
            {
                throw new IllegalStateException("Invalid packet received from server");
            }
        }
    }
}
