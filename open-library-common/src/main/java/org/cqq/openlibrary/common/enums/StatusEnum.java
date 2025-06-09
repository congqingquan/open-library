package org.cqq.openlibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态枚举
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    
    NORMAL,
    ABNORMAL,
    
    ENABLED,
    DISABLED,
    
    FROZEN,
    DELETED,
    
    BOUND,
    UNBOUND,
    
    SUCCESS,
    FAILED,
    PROCESSING,
    COMPLETED
    ;
}
