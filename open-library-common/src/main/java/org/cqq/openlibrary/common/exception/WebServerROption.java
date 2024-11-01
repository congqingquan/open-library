package org.cqq.openlibrary.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.domain.ROption;

/**
 * Web server response option
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum WebServerROption implements ROption {

    SUCCESS(200, "成功"),
    UNAUTHENTICATED(401, "未认证"),
    UNAUTHORIZED(403, "未授权"),
    SERVER_INNER_EXCEPTION(500, "服务内部异常"),
    BUSINESS_EXCEPTION(600, "业务异常"),
    VALIDATED_EXCEPTION(700, "参数验证异常");

    private final Integer code;

    private final String message;
}