package net.mrgregorix.variant.rpc.inject;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

import net.mrgregorix.variant.rpc.core.authenticator.publickey.RsaPublicKeyAuthentication;
import net.mrgregorix.variant.rpc.inject.config.authentications.PublicKeyAuthenticationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPublicKeyAuthenticationConfig
{
    @Test
    public void testLoading() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
    {
        final RsaPublicKeyAuthentication authenticator = new PublicKeyAuthenticationConfig("src/test/resources/test_key.pub", "src/test/resources/test_key").createAuthenticator();

        final String veryPreciousData = "Data, lol";
        final Signature signer = Signature.getInstance("SHA1WithRSA");
        signer.initSign(authenticator.getPrivateKey());
        signer.update(veryPreciousData.getBytes(StandardCharsets.UTF_8));
        final byte[] signature = signer.sign();

        final Signature verifier = Signature.getInstance("SHA1WithRSA");
        verifier.initVerify(authenticator.getPublicKey());
        verifier.update(veryPreciousData.getBytes(StandardCharsets.UTF_8));

        Assertions.assertTrue(verifier.verify(signature), "signature doesn't match");
    }
}
