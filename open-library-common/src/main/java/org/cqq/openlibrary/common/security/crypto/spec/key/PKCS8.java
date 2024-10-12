package org.cqq.openlibrary.common.security.crypto.spec.key;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * PKCS8 key spec
 *
 * @author CongQingquan
 */
public class PKCS8 {
    
    public static PrivateKey privateKey(String algorithm, byte[] encodedKey) throws Exception {
        return KeyFactory
                .getInstance(algorithm)
                .generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }
    
    public static PublicKey publicKey(String algorithm, byte[] encodedKey) throws Exception {
        return KeyFactory
                .getInstance(algorithm)
                .generatePublic(new PKCS8EncodedKeySpec(encodedKey));
    }
}