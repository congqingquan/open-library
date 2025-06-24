package org.cqq.openlibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Register method enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum RegisterMethodEnum {

    MINI_PROGRAM_WECHAT,
    MINI_PROGRAM_ALIPAY,
    APP,
    H5,
    PC
}
