package org.cqq.openlibrary.common.domain.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信 jscode2session 接口出参
 *
 * @author Qingquan
 */
public record JSCode2SessionVO(String openid,
                               @JsonProperty("unionid")
                               String unionId,
                               @JsonProperty("session_key")
                               String sessionKey,
                               @JsonProperty("errcode")
                               int errCode,
                               @JsonProperty("errmsg")
                               String errMsg) {
}