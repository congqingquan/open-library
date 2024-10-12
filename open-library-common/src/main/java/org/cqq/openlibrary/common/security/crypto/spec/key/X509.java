package org.cqq.openlibrary.common.security.crypto.spec.key;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * X509 key spec
 *
 * @author CongQingquan
 */
public class X509 {
    
    public static PrivateKey privateKey(String algorithm, byte[] encodedKey) throws Exception {
        return KeyFactory
                .getInstance(algorithm)
                .generatePrivate(new X509EncodedKeySpec(encodedKey));
    }
    
    public static PublicKey publicKey(String algorithm, byte[] encodedKey) throws Exception {
        return KeyFactory
                .getInstance(algorithm)
                .generatePublic(new X509EncodedKeySpec(encodedKey));
    }
}