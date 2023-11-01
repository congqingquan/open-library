package org.cqq.oplibrary.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.oplibrary.web.entity.ROption;

/**
 * Web server response option
 *
 * @author Qingquan.Cong
 */
@Getter
@AllArgsConstructor
public enum WebServerROption implements ROption {

    SUCCESS(200, "成功"),
    SERVER_INNER_EXCEPTION(500, "服务内部异常"),
    BUSINESS_EXCEPTION(600, "业务异常"),
    VALIDATED_EXCEPTION(700, "参数验证异常");

    private final Integer code;

    private final String message;
}