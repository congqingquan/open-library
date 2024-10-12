package org.cqq.openlibrary.common.security.crypto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Cipher mode
 *
 * @see javax.crypto.Cipher
 *
 * @author CongQingquan
 */
@Getter
@AllArgsConstructor
public enum CipherMode {
    ENCRYPT(1),
    DECRYPT(2),
    WRAP(3),
    UNWRAP(4);

    private final int value;
}