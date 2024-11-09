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
    DISABLED,
    FROZEN,
    DELETED,
    
    SUCCESS,
    FAILED,
    PROCESSING,
    COMPLETED
    ;
}
