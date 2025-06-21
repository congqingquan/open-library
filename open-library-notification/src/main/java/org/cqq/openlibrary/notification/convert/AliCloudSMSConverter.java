package org.cqq.openlibrary.notification.convert;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.cqq.openlibrary.common.component.template.ExceptionTemplate;
import org.cqq.openlibrary.notification.config.AliCloudSMSConfig;
import org.cqq.openlibrary.notification.exception.AliCloudSMSException;

/**
 * Ali cloud SMS converter
 *
 * @author Qingquan
 */
public interface AliCloudSMSConverter {
    
    static Client toClient(AliCloudSMSConfig config) {
        return ExceptionTemplate.execute(
                () -> new Client(
                        new Config()
                                .setAccessKeyId(config.getAccessKeyId())
                                .setAccessKeySecret(config.getAccessKeySecret())
                                .setEndpoint("dysmsapi.aliyuncs.com")
                ),
                (throwable -> new AliCloudSMSException("Building Ali cloud SMS Client failed", throwable))
        );
    }
}
