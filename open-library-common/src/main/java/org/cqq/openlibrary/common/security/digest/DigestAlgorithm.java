package org.cqq.openlibrary.common.security.digest;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 摘要算法
 *
 * @author CongQingquan
 */
@Getter
@AllArgsConstructor
public enum DigestAlgorithm {
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");

    private final String value;
}