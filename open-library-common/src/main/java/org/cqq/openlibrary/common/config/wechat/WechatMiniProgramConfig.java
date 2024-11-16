package org.cqq.openlibrary.common.config.wechat;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 微信证书配置
 *
 * @author Qingquan
 */
@Data
public class WechatMiniProgramConfig {
    
    /**
     * 商户应用 id
     */
    @Value("${wechat.merchant.appid:none}")
    private String merchantAppid;
    
    /**
     * 商户应用密钥
     */
    @Value("${wechat.merchant.app-secret:none}")
    private String merchantAppSecret;
    
}