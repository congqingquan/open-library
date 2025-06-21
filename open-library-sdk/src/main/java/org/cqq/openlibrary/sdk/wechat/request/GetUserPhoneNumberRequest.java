package org.cqq.openlibrary.sdk.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.sdk.wechat.request.base.WechatPostRequest;
import org.cqq.openlibrary.sdk.wechat.response.GetUserPhoneNumberResponse;

import java.util.function.Supplier;

/**
 * 获取用户手机号 API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetUserPhoneNumberRequest extends WechatPostRequest<GetUserPhoneNumberResponse> {
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-info/phone-number/getPhoneNumber.html
    
    private String code;
    
    @Override
    public WechatApiEnum apiEnum() {
        return WechatApiEnum.GET_USER_PHONE_NUMBER;
    }
    
    @Override
    public Supplier<GetUserPhoneNumberResponse> responseSupplier() {
        return GetUserPhoneNumberResponse::new;
    }
}
