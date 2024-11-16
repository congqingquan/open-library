package org.cqq.openlibrary.notification.convert;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.cqq.openlibrary.common.component.template.ExceptionTemplate;
import org.cqq.openlibrary.common.config.aliyun.AliYunSMSConfig;
import org.cqq.openlibrary.common.exception.AliYunException;

/**
 * 阿里云转换器
 *
 * @author Qingquan
 */
public interface AliYunConverter {
    
    static Client smsConfig2Client(AliYunSMSConfig config) {
        return ExceptionTemplate.execute(
                () -> new Client(
                        new Config()
                                .setAccessKeyId(config.getAccessKeyId())
                                .setAccessKeySecret(config.getAccessKeySecret())
                                .setEndpoint("dysmsapi.aliyuncs.com")
                ),
                (throwable -> new AliYunException("构建 SMS Client 失败", throwable))
        );
    }
}
