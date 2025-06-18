package org.cqq.openlibrary.storage.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.storage.Storage;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.handler.replaceurl.ReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.request.DefaultRequestReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.request.RequestReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.response.REntityResponseReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.response.ResponseReplaceURLHandler;
import org.cqq.openlibrary.storage.local.LocalStorage;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Storage auto configure
 *
 * @author Qingquan
 */
@Slf4j
@ConditionalOnProperty(name = "storage.active")
@Configuration
@EnableConfigurationProperties(StorageConfig.class)
public class StorageAutoConfigure {
    
    // ======================================== Local ========================================
    
    @ConditionalOnProperty(name = "storage.active", havingValue = "LOCAL")
    @Bean
    public Storage localStorage(StorageConfig config) {
        log.info("Building local storage, config info [{}]", JSONUtils.toJSONString(config));
        LocalStorageConfig local = config.getLocal();
        LocalStorageConfig.Server server = local.getServer();
        LocalStorage localStorage = new LocalStorage(local.getRootDir(), local.getWriteDir(), server.getProtocol(), server.getHost(), server.getPort());
        log.info("Build local storage successfully");
        return localStorage;
    }
    
    // ======================================== Web ========================================
    
    @ConditionalOnProperty(name = "storage.replace-url-handler.enable", havingValue = "true")
    public static class RequestReplaceURLHandlerConfig {
        
        @Bean
        public DefaultRequestReplaceURLHandler defaultRequestReplaceURLHandler(StorageProvider storageProvider) {
            return new DefaultRequestReplaceURLHandler(storageProvider);
        }
        
        @Bean
        public REntityResponseReplaceURLHandler replaceUrlHandler(StorageProvider storageProvider) {
            return new REntityResponseReplaceURLHandler(storageProvider);
        }
        
        @Bean
        public Advisor replaceUrlAdvisor(StorageConfig storageConfig,
                                         Collection<RequestReplaceURLHandler> requestReplaceURLHandlers,
                                         Collection<ResponseReplaceURLHandler> responseReplaceUrlHandlers) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression(storageConfig.getReplaceUrlHandler().getPointcutExpression());
            return new DefaultPointcutAdvisor(pointcut, new ReplaceURLHandler(requestReplaceURLHandlers, responseReplaceUrlHandlers));
        }
    }
}
