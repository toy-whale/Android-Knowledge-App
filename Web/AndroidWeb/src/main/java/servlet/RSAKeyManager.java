package servlet;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

    // store priKey and pubKey
    private static Map<Integer, String> keyMap = new HashMap<>();

    private static final int KEY_LENGTH = 1024;
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = KEY_LENGTH / 8;

    public static void generate() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            assert generator != null;
            
            SecureRandom secRandom = new SecureRandom();
            secRandom.setSeed(new Date().getTime());
            generator.initialize(KEY_LENGTH, secRandom);

            // get Keys
            KeyPair pair = generator.generateKeyPair();
            RSAPublicKey pubKey = (RSAPublicKey) pair.getPublic();
            RSAPrivateKey priKey = (RSAPrivateKey) pair.getPrivate();

            // save into Map
            String publicKeyString = new String(Base64.encodeBase64(pubKey.getEncoded()));
            String privateKeyString = new String(Base64.encodeBase64(priKey.getEncoded()));
            keyMap.put(0, publicKeyString);
            keyMap.put(1, privateKeyString);
            // 0 for public
            // 1 for private

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
    public static String getPublicKey() {
        return keyMap.get(0);
    }

    public static String getPrivateKey() {
        return keyMap.get(1);
    }

    // use publicKey to encrypt str
    public static String encrypt(String str, String publicKey) {

        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = null;
        String cipherStr = null;

        try {
            pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            cipherStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // RSA encrypt string
        return cipherStr;
    }

    // this method is not necessary in client
    // use encrypt string and privateKey to decrypt str
    public static String decrypt(String str, String privateKey) throws IOException {

    	byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = null;
        String clearStr = null;

        try {
            priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            
            // 128 bytes part decrypt
            int inputLength = inputByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0; int i = 0;
            byte[] cache;
            
            while (inputLength - offset > 0) {
            	if (inputLength - offset > MAX_DECRYPT_BLOCK) {
            		cache = cipher.doFinal(inputByte, offset, MAX_DECRYPT_BLOCK);
            	} else {
            		cache = cipher.doFinal(inputByte, offset, inputLength - offset);
            	}
            	out.write(cache, 0, cache.length);
            	i++;
            	offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] clearData = out.toByteArray();
            out.close();
            clearStr = new String(clearData, "UTF-8");
            
            //clearStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }

        // RSA decrypt string
        return clearStr;
    }


}
