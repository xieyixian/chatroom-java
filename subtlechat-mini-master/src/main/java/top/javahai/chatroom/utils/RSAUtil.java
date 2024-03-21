package top.javahai.chatroom.utils;



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


/**
 * 备注，解密前台公钥加密的数据，请调用decryptWithPrivate方法
 * 每次重启之后，都会生成一个一对新的公私钥
 */
public class RSAUtil {

    //秘钥大小
    private static final int KEY_SIZE = 1024;

    //后续放到常量类中去
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PUBLIC_KEY = "publicKey";

    private static KeyPair keyPair;

    private static Map<String, String> rsaMap;

    private static org.bouncycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = null;

    //BouncyCastleProvider内的方法都为静态方法，GC不会回收
    public static synchronized org.bouncycastle.jce.provider.BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }

    //生成RSA，并存放
    static {
        try {

            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", getInstance());
            SecureRandom random = new SecureRandom();
            generator.initialize(KEY_SIZE, random);
            keyPair = generator.generateKeyPair();
            //将公钥和私钥存放，登录时会不断请求获取公钥
            //建议放到redis的缓存中，避免在分布式场景中，出现拿着server1的公钥去server2解密的问题
            storeRSA();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将RSA存入缓存
     */
    private static void storeRSA() {
        rsaMap = new HashMap<>();
        PublicKey publicKey = keyPair.getPublic();
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        rsaMap.put(PUBLIC_KEY, publicKeyStr);

        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));
        rsaMap.put(PRIVATE_KEY, privateKeyStr);
    }

    /**
     * 私钥解密(解密前台公钥加密的密文)
     *
     * @param encryptText 公钥加密的数据
     * @return 私钥解密出来的数据
     * @throws Exception e
     */
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

    /**
     * java端 使用公钥加密(此方法暂时用不到)
     *
     * @param plaintext 明文内容
     * @return byte[]
     * @throws UnsupportedEncodingException e
     */
    public static byte[] encrypt(String plaintext) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(plaintext, "utf-8");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        //获取公钥指数
        BigInteger e = rsaPublicKey.getPublicExponent();
        //获取公钥系数
        BigInteger n = rsaPublicKey.getModulus();
        //获取明文字节数组
        BigInteger m = new BigInteger(encode.getBytes());
        //进行明文加密
        BigInteger res = m.modPow(e, n);
        return res.toByteArray();

    }

    /**
     * java端 使用私钥解密(此方法暂时用不到)
     *
     * @param cipherText 加密后的字节数组
     * @return 解密后的数据
     * @throws UnsupportedEncodingException e
     */
    public static String decrypt(byte[] cipherText) throws UnsupportedEncodingException {
        RSAPrivateKey prk = (RSAPrivateKey) keyPair.getPrivate();
        // 获取私钥参数-指数/系数
        BigInteger d = prk.getPrivateExponent();
        BigInteger n = prk.getModulus();
        // 读取密文
        BigInteger c = new BigInteger(cipherText);
        // 进行解密
        BigInteger m = c.modPow(d, n);
        // 解密结果-字节数组
        byte[] mt = m.toByteArray();
        //转成String,此时是乱码
        String en = new String(mt);
        //再进行编码,最后返回解密后得到的明文
        return URLDecoder.decode(en, "UTF-8");
    }


    /**
     * 使用私钥加密
     *
     * @param plaintext 明文内容
     * @return 加密后的字符串
     * @throws Exception 如果加密过程中发生异常
     */
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
    /**
     * 获取公钥
     *
     * @return 公钥
     */
    public static String getPublicKey() {
        return rsaMap.get(PUBLIC_KEY);
    }

    /**
     * 获取私钥
     *
     * @return 私钥
     */
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