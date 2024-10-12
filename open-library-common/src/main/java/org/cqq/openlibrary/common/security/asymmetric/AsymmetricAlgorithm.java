package org.cqq.openlibrary.common.security.asymmetric;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 非对称加密算法
 *
 * @author CongQingquan
 */
@Getter
@AllArgsConstructor
public enum AsymmetricAlgorithm {
    RSA("RSA"),
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding"),
    RSA_ECB("RSA/ECB/NoPadding"),
    RSA_None("RSA/None/NoPadding");

    private final String value;
}