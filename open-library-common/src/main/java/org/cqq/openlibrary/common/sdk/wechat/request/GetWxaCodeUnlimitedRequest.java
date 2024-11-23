package org.cqq.openlibrary.common.sdk.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.common.sdk.wechat.WechatApiEnum;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatPostRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.GetWxaCodeUnlimitedResponse;

import java.util.function.Supplier;

/**
 * 获取不限制的小程序码 API 请求
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GetWxaCodeUnlimitedRequest extends WechatPostRequest<GetWxaCodeUnlimitedResponse> {
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
    
    private String scene;
    private String page;
    @JsonProperty("check_path")
    private Boolean checkPath;
    @JsonProperty("env_version")
    private String envVersion;
    private String width;
    @JsonProperty("auto_color")
    private String autoColor;
    @JsonProperty("line_color")
    private String lineColor;
    @JsonProperty("is_hyaline")
    private String isHyaLine;
    
    @Override
    public WechatApiEnum apiEnum() {
        return WechatApiEnum.GET_WXA_CODE_UNLIMITED;
    }
    
    @Override
    public Supplier<GetWxaCodeUnlimitedResponse> responseSupplier() {
        return GetWxaCodeUnlimitedResponse::new;
    }
}
