package cn.ruiheyun.athena.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RSAUtils {

    public static final String PUBLIC_KEY = "PublicKey";
    public static final String PRIVATE_KEY = "PrivateKey";

    /**
     * 获取密钥对(初始化密钥对)
     * @return
     */
    public static KeyPair getKeyPair() {
        return getKeyPair(2048);
    }

    /**
     * 获取密钥对(初始化密钥对)
     * @param keySize
     * @return
     */
    public static KeyPair getKeyPair(int keySize) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(keySize);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.error("捕获异常, 异常类型: {}, 异常消息: {}", e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
        return keyPair;
    }

    /**
     * 获取公钥 base64 字符串
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return CommonUtils.byteArrayToBase64(publicKey.getEncoded());
    }

    /**
     * 获取私钥 base64 字符串
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        return CommonUtils.byteArrayToBase64(privateKey.getEncoded());
    }

    /**
     * base 64 字符串 转 公钥
     * @param base64PublicKey
     * @return
     */
    public static PublicKey base64ToPublicKey(String base64PublicKey) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(CommonUtils.base64ToByteArray(base64PublicKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64 字符串 转 私钥
     * @param base64PrivateKey
     * @return
     */
    public static PrivateKey base64ToPrivateKey(String base64PrivateKey) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(CommonUtils.base64ToByteArray(base64PrivateKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 公钥加密(分段加密)
     * @param src
     * @param publicKey
     * @return
     */
    public static String publicKeyEncrypt(String src, PublicKey publicKey) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] inputData = src.getBytes(StandardCharsets.UTF_8);
            int offSet;
            int i = 0;
            while ((inputData.length - (offSet = (i * 256))) > 0) {
                byte[] cache;
                if (inputData.length - offSet > 256) {
                    cache = cipher.doFinal(inputData, offSet, 256);
                } else {
                    cache = cipher.doFinal(inputData, offSet, inputData.length - offSet);
                }
                out.write(cache, 0, cache.length);
                ++ i;
            }
            return out.toString();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IOException e) {
            log.error("捕获异常, 异常类型: {}, 异常消息: {}", e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 私钥解密(分段解密)
     * @param src
     * @param privateKey
     * @return
     */
    public static String privateKeyDecrypt(String src, PrivateKey privateKey) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] inputData = CommonUtils.base64ToByteArray(src);
            int offSet;
            int i = 0;
            while ((inputData.length - (offSet = (i * 256))) > 0) {
                byte[] cache;
                if (inputData.length - offSet > 256) {
                    cache = cipher.doFinal(inputData, offSet, 256);
                } else {
                    cache = cipher.doFinal(inputData, offSet, inputData.length - offSet);
                }
                out.write(cache, 0, cache.length);
                ++ i;
            }
            return out.toString();
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException | IOException e) {
            log.error("捕获异常, 异常类型: {}, 异常消息: {}", e.getClass(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
