package org.cqq.openlibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型枚举
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    MINI_PROGRAM_WECHAT,
    MINI_PROGRAM_ALIPAY,
    APP,
    PC
}
