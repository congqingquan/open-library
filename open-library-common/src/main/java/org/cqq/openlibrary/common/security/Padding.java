package org.cqq.openlibrary.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Padding
 *
 * @author CongQingquan
 */
@Getter
@AllArgsConstructor
public enum Padding {
    NoPadding,
    ZeroPadding,
    ISO10126Padding,
    OAEPPadding,
    PKCS1Padding,
    PKCS5Padding,
    SSL3Padding
    ;
}