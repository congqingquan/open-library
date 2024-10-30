package org.cqq.openlibrary.common.security.crypto.spec.key;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Secret key spec
 *
 * @author Qingquan
 */
public class SecretKey {
    
    public static Key create(String algorithm, byte[] encodedKey) throws Exception {
        return new SecretKeySpec(encodedKey, algorithm);
    }
}