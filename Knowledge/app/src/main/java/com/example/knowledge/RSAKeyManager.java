package com.example.knowledge;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.binary.Base64;


public class RSAKeyManager {

    // use publicKey to encrypt str
    public static String encrypt(String str, String publicKey) {

        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = null;
        String cipherStr = null;

        try {
            pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // RSA encrypt string
        return cipherStr;
    }

    // this method is not necessary in client


}
