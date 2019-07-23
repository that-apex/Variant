package net.mrgregorix.variant.rpc.inject.config.authentications;

import java.io.FileReader;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.mrgregorix.variant.rpc.core.authenticator.publickey.RsaPublicKeyAuthentication;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentication;
import net.mrgregorix.variant.utils.annotation.Nullable;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

/**
 * A {@link RpcConfigAuthentication} for {@link RsaPublicKeyAuthentication}
 */
public class PublicKeyAuthenticationConfig extends RpcConfigAuthentication<RsaPublicKeyAuthentication>
{
    @JacksonXmlProperty(localName = "publicKey", isAttribute = true)
    @Nullable
    private String publicKey;

    @JacksonXmlProperty(localName = "privateKey", isAttribute = true)
    @Nullable
    private String privateKey;

    public PublicKeyAuthenticationConfig()
    {
    }

    public PublicKeyAuthenticationConfig(@Nullable final String publicKey, @Nullable final String privateKey)
    {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Nullable
    public String getPublicKey()
    {
        return this.publicKey;
    }

    @Nullable
    public String getPrivateKey()
    {
        return this.privateKey;
    }

    @Override
    public RsaPublicKeyAuthentication createAuthenticator()
    {
        final JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

        RSAPublicKey publicKey = null;
        RSAPrivateKey privateKey = null;

        if (this.publicKey != null)
        {
            try (final FileReader fileReader = new FileReader(this.publicKey))
            {
                final Object publicKeyInfo = new PEMParser(fileReader).readObject();

                if (! (publicKeyInfo instanceof SubjectPublicKeyInfo))
                {
                    throw new IllegalArgumentException(this.publicKey + " is not a valid public key");
                }

                publicKey = (RSAPublicKey) converter.getPublicKey((SubjectPublicKeyInfo) publicKeyInfo);
            }
            catch (final Exception e)
            {
                throw new RuntimeException("Cannot read public key " + this.getPublicKey(), e);
            }
        }

        if (this.privateKey != null)
        {
            try
            {
                try (final FileReader fileReader = new FileReader(this.privateKey))
                {
                    final Object object = new PEMParser(fileReader).readObject();

                    if (object instanceof PEMKeyPair)
                    {
                        final KeyPair keyPair = converter.getKeyPair((PEMKeyPair) object);
                        privateKey = (RSAPrivateKey) keyPair.getPrivate();

                        if (publicKey == null)
                        {
                            publicKey = (RSAPublicKey) keyPair.getPublic();
                        }
                    }
                    else if (object instanceof PrivateKeyInfo)
                    {
                        privateKey = (RSAPrivateKey) converter.getPrivateKey((PrivateKeyInfo) object);
                    }
                    else
                    {
                        throw new IllegalArgumentException(this.publicKey + " is not a valid private key");
                    }
                }
            }
            catch (final Exception e)
            {
                throw new RuntimeException("Cannot read private key " + this.getPrivateKey(), e);
            }
        }

        if (publicKey == null)
        {
            throw new IllegalArgumentException("no public key found");
        }

        return new RsaPublicKeyAuthentication(publicKey, privateKey);
    }
}
