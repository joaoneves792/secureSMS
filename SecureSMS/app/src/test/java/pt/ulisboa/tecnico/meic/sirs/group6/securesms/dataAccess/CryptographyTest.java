package pt.ulisboa.tecnico.meic.sirs.group6.securesms.dataAccess;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import pt.ulisboa.tecnico.meic.sirs.group6.securesms.domain.Cryptography;
import pt.ulisboa.tecnico.meic.sirs.group6.securesms.domain.exceptions.FailedToSignException;
import pt.ulisboa.tecnico.meic.sirs.group6.securesms.domain.exceptions.FailedToVerifySignatureException;
import pt.ulisboa.tecnico.meic.sirs.group6.securesms.domain.exceptions.InvalidSignatureException;

/**
 * Created by lribeirogomes on 18/11/15.
 */
public class CryptographyTest extends TestCase {

    private PrivateKey _ecPrivateKey, _rsaPrivateKey;
    private PublicKey _ecPublicKey, _rsaPublicKey;
    private SecretKey _aesKey;

    private byte[] _plaintext;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp()throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchProviderException {

        _plaintext = "this is a sample text".getBytes("UTF-8");

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(224, new SecureRandom());
        KeyPair ECKeyPair = keyGen.generateKeyPair();
        _ecPrivateKey = ECKeyPair.getPrivate();
        _ecPublicKey = ECKeyPair.getPublic();

        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048, new SecureRandom());
        KeyPair RSAKeyPair = keyGen.generateKeyPair();
        _rsaPrivateKey = RSAKeyPair.getPrivate();
        _rsaPublicKey = RSAKeyPair.getPublic();

        KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
        aesKeyGen.init(128, new SecureRandom());
        _aesKey = aesKeyGen.generateKey();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSignature() throws Exception {
        byte[] signature = Cryptography.sign(_plaintext, _ecPrivateKey);
        Cryptography.verifySignature(_plaintext, signature, _ecPublicKey);
    }

    @Test(expected = InvalidSignatureException.class)
    public void testBadSignature() throws Exception{//Not entirely deterministic!
        byte[] signature = Cryptography.sign(_plaintext, _ecPrivateKey);
        signature[10] = 0;
        signature[11] = 0;
        signature[12] = 0;
        signature[13] = 0;
        try{
            Cryptography.verifySignature(_plaintext, signature, _ecPublicKey);
            assertTrue(false);
        }catch (InvalidSignatureException e){
            //For some reason the expected=Exception.class annotation is not working...
        }
    }

    @Test
    public void testSymmetricEncryption()throws Exception{
        byte[] cipheredData = Cryptography.symmetricCipher(_plaintext, _aesKey);
        byte[] plaintext = Cryptography.symmetricDecipher(cipheredData, _aesKey);
        assertTrue(Arrays.equals(plaintext, _plaintext));
    }
}