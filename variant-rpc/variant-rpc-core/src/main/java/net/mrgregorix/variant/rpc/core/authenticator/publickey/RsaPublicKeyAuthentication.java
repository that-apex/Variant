package net.mrgregorix.variant.rpc.core.authenticator.publickey;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.core.authenticator.PublicKeyAuthentication;
import net.mrgregorix.variant.utils.io.ByteArrayUtils;

/**
 * A {@link RpcAuthenticator} that used RSA public-key cryptography to authenticate clients.
 */
public class RsaPublicKeyAuthentication extends PublicKeyAuthentication<RSAPublicKey, RSAPrivateKey>
{
    private static final byte[] TAG = "variant-rsa".getBytes();

    /**
     * Creates a new RsaPublicKeyAuthentication, private key is required only for clients
     *
     * @param publicKey  public key
     * @param privateKey private key
     */
    public RsaPublicKeyAuthentication(final RSAPublicKey publicKey, final RSAPrivateKey privateKey)
    {
        super(publicKey, privateKey);
    }

    /**
     * Creates a new PublicKeyAuthentication with no private key, this can be only used as a server {@link RpcAuthenticator}
     *
     * @param publicKey public key
     */
    public RsaPublicKeyAuthentication(final RSAPublicKey publicKey)
    {
        super(publicKey);
    }

    @Override
    protected byte[] computeFingerprint()
    {
        final byte[] rawBytes;
        try
        {
            final byte[] modulus = this.getPublicKey().getModulus().toByteArray();
            final byte[] publicExponent = this.getPublicKey().getPublicExponent().toByteArray();

            rawBytes = ByteArrayUtils.prepareDataByteArray(data -> {
                data.writeInt(TAG.length);
                data.write(TAG);
                data.writeInt(publicExponent.length);
                data.write(publicExponent);
                data.writeInt(modulus.length);
                data.write(modulus);
            });
        }
        catch (final IOException e)
        {
            throw new AssertionError("This should never happen");
        }

        try
        {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(rawBytes);
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new UnsupportedOperationException("No SHA256 digest", e);
        }
    }

    @Override
    protected byte[] signNonce(final byte[] nonce)
    {
        final Signature sig = this.getSignature();
        try
        {
            sig.initSign(Objects.requireNonNull(this.getPrivateKey(), "private key"), SECURE_RANDOM);
            sig.update(nonce);
            return sig.sign();
        }
        catch (final InvalidKeyException | SignatureException e)
        {
            throw new RuntimeException("failed to sign nonce", e);
        }
    }

    @Override
    protected boolean validateNonce(final byte[] actualNonce, final byte[] signature)
    {
        final Signature sig = this.getSignature();
        try
        {
            sig.initVerify(this.getPublicKey());
            sig.update(actualNonce);
            return sig.verify(signature);
        }
        catch (final InvalidKeyException | SignatureException e)
        {
            throw new RuntimeException("failed to validate nonce", e);
        }
    }

    /**
     * Gets a {@link Signature} instance that will be used to sign and verify nonces
     *
     * @return a {@link Signature} instance to be used
     */
    protected Signature getSignature()
    {
        try
        {
            return Signature.getInstance("SHA1WithRSA");
        }
        catch (final NoSuchAlgorithmException e)
        {
            throw new UnsupportedOperationException("No SHA1WithRSA signature instance", e);
        }
    }
}
