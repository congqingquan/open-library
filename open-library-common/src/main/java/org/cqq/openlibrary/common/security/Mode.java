package org.cqq.openlibrary.common.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Mode
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum Mode {
    NONE,
    CBC,
    CFB,
    CTR,
    CTS,
    ECB,
    OFB,
    PCBC
    ;
}