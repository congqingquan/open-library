package org.cqq.openlibrary.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum PaymentStatusEnum {
    
    UNPAID,
    PAID,
    CANCELED,
    TIMOUT,
    FAILED
    ;
}