package org.cqq.openlibrary.common.util.encrypt;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Blowfish 加解密
 *
 * @author CongQingquan
 */
public class BlowfishUtils {

    /**
     * Blowfish 加密
     *
     * @param mode    工作模式
     * @param padding 填充模式
     * @param key     密钥
     * @param content 待加密数据
     * @return 加密后的密文
     */
    public static byte[] encrypt(Mode mode, Padding padding, String key, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance("BLOWFISH/" + mode.getValue() + "/" + padding.getValue());
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "Blowfish"));
            return cipher.doFinal(content);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Encrypt error", exception);
        }
    }

    /**
     * Blowfish 解密
     *
     * @param mode    工作模式
     * @param padding 填充模式
     * @param key     密钥
     * @param content 待解密的密文
     * @return 解密后的原文
     */
    public static byte[] decrypt(Mode mode, Padding padding, String key, byte[] content) {
        try {
            Cipher cipher = Cipher.getInstance("BLOWFISH/" + mode.getValue() + "/" + padding.getValue());
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "Blowfish"));
            return cipher.doFinal(content);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Decrypt error", exception);
        }
    }

    // 工作模式
    @Getter
    @AllArgsConstructor
    public enum Mode {
        ECB("ECB");
        
        private final String value;
    }

    // 填充模式
    @Getter
    @AllArgsConstructor
    public enum Padding {

        NO_PADDING("NoPadding"),
        
        PKCS5_PADDING("PKCS5Padding");

        private final String value;
    }
}