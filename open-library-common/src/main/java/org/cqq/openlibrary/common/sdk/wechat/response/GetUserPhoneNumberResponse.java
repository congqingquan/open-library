package org.cqq.openlibrary.common.sdk.wechat.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.common.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.JSONUtils;

/**
 * GetUserPhoneNumber API 响应
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetUserPhoneNumberResponse extends WechatResponse<GetUserPhoneNumberResponse> {
    
    @JsonProperty("phone_info")
    private PhoneInfo phoneInfo;
    
    @Data
    public static class PhoneInfo {
        // 用户绑定的手机号（国外手机号会有区号）
        private String phoneNumber;
        // 没有区号的手机号
        private String purePhoneNumber;
        // 区号
        private String countryCode;
        
        @Data
        public static class WaterMark {
            // 用户获取手机号操作的时间戳
            private Long timestamp;
            // 小程序appid
            private String appid;
        }
    }
    
    @Override
    public CheckedFunction<ResponseBody, GetUserPhoneNumberResponse, ?> parseResponseMapper() {
        return responseBody -> JSONUtils.parse(responseBody.string(), GetUserPhoneNumberResponse.class);
    }
}
