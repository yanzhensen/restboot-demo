package com.sam.common.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.net.URLDecoder;

/**
 * @Author: yjw
 * @CreateTime: 2020-06-15 14:51
 * @Description: 数据库密码加密与解密
 */
public class PasswordUtils {
    /**
     * 加密
     *
     * @param plainText
     * @return
     */
    public static String encrypt(String plainText) {
        String result = "";
        try {
            DESKeySpec keySpec = new DESKeySpec("CampusHelper".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Encoder base64encoder = new sun.misc.BASE64Encoder();
            // ENCODE plainTextPassword String
            byte[] cleartext = plainText.getBytes("UTF8");
            // cipher is not thread
            Cipher cipher = Cipher.getInstance("DES");
            // safe
            cipher.init(Cipher.ENCRYPT_MODE, key);
            result = base64encoder.encode(cipher.doFinal(cleartext));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解密
     *
     * @param encryptedPwd
     * @return
     */
    public static String decrypt(String encryptedPwd) {
        String result = "";
        try {
            DESKeySpec keySpec = new DESKeySpec("CampusHelper".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Decoder base64decoder = new sun.misc.BASE64Decoder();
            // DECODE encryptedPwd String
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encryptedPwd);
            // cipher is not thread
            Cipher cipher = Cipher.getInstance("DES");
            // safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainTextPwdBytes = (cipher.doFinal(encrypedPwdBytes));
            result = new String(plainTextPwdBytes);
        } catch (Exception e) {
            System.out.println("解密失败:" + encryptedPwd);
            result = againDecrypt(encryptedPwd);
        }
        return result;
    }

    /**
     * 重新解密
     *
     * @param encryptedPwd
     * @return
     */
    public static String againDecrypt(String encryptedPwd) {
        String result = "";
        try {
            encryptedPwd = URLDecoder.decode(encryptedPwd, "utf-8");
            DESKeySpec keySpec = new DESKeySpec("CampusHelper".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            sun.misc.BASE64Decoder base64decoder = new sun.misc.BASE64Decoder();
            // DECODE encryptedPwd String
            byte[] encrypedPwdBytes = base64decoder.decodeBuffer(encryptedPwd);
            // cipher is not thread
            Cipher cipher = Cipher.getInstance("DES");
            // safe
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] file = cipher.doFinal(encrypedPwdBytes);
            byte[] plainTextPwdBytes = (file);
            result = new String(plainTextPwdBytes);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("重新解密失败:" + encryptedPwd);
        }
        return result;
    }

    public static void main(String[] args) {
        String pas = encrypt("WWW86xcnet");
        System.out.println(pas);
        System.out.println(decrypt(pas));
    }
}
