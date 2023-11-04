package org.cqq.oplibrary.web.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWS Token status
 *
 * @author Qingquan.Cong
 */
@Getter
@AllArgsConstructor
public enum JWSTokenStatus {

    /**
     * 有效：成功解析
     */
    VALID,
    /**
     * 无效：解析失败
     */
    INVALID,
    /**
     * 当前时间已大于Token的Payload中的Expiration字段标识的过期时间戳
     * &&
     * 过期时间量已经超过 parserBuilder().setAllowedClockSkewSeconds(n) 设定的参数 n
     */
    EXPIRED;
}