package org.cqq.openlibrary.trade.autoconfigure;

import lombok.Data;
import org.cqq.openlibrary.trade.config.alipay.AlipayPaymentConfig;
import org.cqq.openlibrary.trade.config.wechat.WeChatPaymentConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Trade config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.trade")
public class TradeConfig {
    
    private AlipayPaymentConfig alipayPaymentConfig;
    
    private WeChatPaymentConfig wechatPaymentConfig;
}
