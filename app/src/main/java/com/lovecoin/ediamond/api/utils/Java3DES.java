package com.lovecoin.ediamond.api.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Java3DES {
    // 算法名称
    private static final String KEY_ALGORITHM = "desede";
    // 算法名称/加密模式/填充方式
    private static final String CIPHER_ALGORITHM = "desede/CBC/PKCS5Padding";

    /**
     * CBC加密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  明文
     * @return
     * @throws Exception
     */
    public static byte[] des3EncodeCBC(String key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        return cipher.doFinal(data);
    }

    /**
     * 生成密钥key对象
     * v
     *
     * @return 密钥对象
     */
    private static Key keyGenerator(String keyStr) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(keyStr.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyfactory.generateSecret(spec);
    }

    /**
     * 3des解密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  编码的密文
     * @return 明文
     * @throws Exception
     */
    public static byte[] des3DecodeCBC(String key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        return cipher.doFinal(data);
    }

}