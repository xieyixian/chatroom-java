package top.team7.chatroom.utils;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;



public class RSAUtil {

    private static final int KEY_SIZE = 1024;

    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";

    private static KeyPair keyPair;

    private static Map<String, String> rsaMap;

    private static org.bouncycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = null;


    public static synchronized org.bouncycastle.jce.provider.BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }


    static {
        try {

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", getInstance());
            SecureRandom random = new SecureRandom();
            generator.initialize(KEY_SIZE, random);
            keyPair = generator.generateKeyPair();

            storeRSA();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private static void storeRSA() {
        rsaMap = new HashMap<>();
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        rsaMap.put(PUBLIC_KEY, publicKeyStr);

        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
        rsaMap.put(PRIVATE_KEY, privateKeyStr);
    }


    public static String decryptWithPrivate(String encryptText) throws Exception {
        if (StringUtils.isBlank(encryptText)) {
            return null;
        }
        byte[] en_byte = Base64.decodeBase64(encryptText.getBytes());

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", getInstance());
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println("Private key is: "+privateKey.toString());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] res = cipher.doFinal(en_byte);
        return new String(res);
    }


    public static byte[] encrypt(String plaintext) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(plaintext, "utf-8");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        //Get public key index
        BigInteger e = rsaPublicKey.getPublicExponent();
        //Get public key coefficient
        BigInteger n = rsaPublicKey.getModulus();
        //Get plaintext byte array
        BigInteger m = new BigInteger(encode.getBytes());
        //Encrypt plain text
        BigInteger res = m.modPow(e, n);
        return res.toByteArray();

    }


    public static String decrypt(byte[] cipherText) throws UnsupportedEncodingException {
        RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
        // Get private key parameters-exponent/coefficient
        BigInteger d = prk.getPrivateExponent();
        BigInteger n = prk.getModulus();
        // Read ciphertext
        BigInteger c = new BigInteger(cipherText);
        // Decrypt
        BigInteger m = c.modPow(d, n);
        // Decryption result - byte array
        byte[] mt = m.toByteArray();
        //Convert to String, which is garbled at this time
        String en = new String(mt);
        //Then encode, and finally return the decrypted plaintext.
        return URLDecoder.decode(en, "UTF-8");
    }


    public static String encryptWithPrivate(String plaintext) throws Exception {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("Plaintext cannot be null or empty");
        }

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        PrivateKey privateKey = keyPair.getPrivate();
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

        return Base64.encodeBase64String(cipherText);
    }

    public static String getPublicKey() {
        return rsaMap.get(PUBLIC_KEY);
    }


    public static String getPrivateKey() {
        return rsaMap.get(PRIVATE_KEY);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(RSAUtil.getPrivateKey());
        System.out.println(RSAUtil.getPublicKey());
        byte[] usernames = RSAUtil.encrypt("username");
        System.out.println(RSAUtil.decrypt(usernames));
    }
}