package org.cqq.openlibrary.common.security.symmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对称加密算法
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum SymmetricAlgorithm {
    AES("AES"),
    ARCFOUR("ARCFOUR"),
    Blowfish("Blowfish"),
    DES("DES"),
    DESede("DESede"),
    RC2("RC2"),
    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede"),
    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40");

    private final String value;
}
