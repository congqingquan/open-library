package org.cqq.openlibrary.sdk.wechat.request.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.sdk.wechat.response.base.WechatResponse;

/**
 * 微信 POST 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class WechatPostRequest<Response extends WechatResponse<Response>> extends WechatRequest<Response> {


}
