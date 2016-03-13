package com.lendou;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AESCoder {
    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "AES";

    /**
     * 加密解密算法/工作模式/填充方式
     * Java6支持PKCS5Padding填充方式
     */
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key密钥
     */
    private static Key toKey(byte[] key) {
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key 密钥
     * @return byte[]解密数据
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception {

        // 还原密钥
        Key k = toKey(key);

        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /***
     * 加密
     *
     * @param data 待加密数据
     * @param key 密钥
     * @return byte[]加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 还原密钥
        Key k = toKey(key);

        // 实例化
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);

        return cipher.doFinal(data);
    }


    /**
     * 生成密钥
     *
     * @return byte[]二进制密钥
     */
    public static byte[] initKey() throws NoSuchAlgorithmException {
        // 实例化
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

        // AES要求密钥长度为128位, 192位或者256位
        kg.init(128);

        // 生成私密密钥
        SecretKey secretKey = kg.generateKey();

        // 获得密钥的二进制编码形式
        return secretKey.getEncoded();
    }

    public static void main(String[] args) throws Exception {

        // 密钥
        byte[] key = AESCoder.initKey();
        System.out.println("key: " + Base64.encodeBase64String(key));

        String contentStr = RandomStringUtils.randomAlphabetic(10);
        byte[] content = contentStr.getBytes("UTF-8");
        System.out.println("origin: " + Base64.encodeBase64String(content));

        // 加密
        byte[] encryptedContent = AESCoder.encrypt(content, key);
        System.out.println("encrypted: " + Base64.encodeBase64String(encryptedContent).toString());

        // 解密
        byte[] decryptedContent = AESCoder.decrypt(encryptedContent, key);
        System.out.println("decrypted: " + Base64.encodeBase64String(decryptedContent));

        // 校验
        System.out.println("equals: " + Arrays.equals(content, decryptedContent));
    }
}
