package org.cqq.openlibrary.storage.autoconfigure;

import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.handler.replaceurl.ReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.request.DefaultRequestReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.request.RequestReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.response.REntityResponseReplaceURLHandler;
import org.cqq.openlibrary.storage.handler.replaceurl.response.ResponseReplaceURLHandler;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Storage replace url handler configure
 *
 * @author Qingquan
 */
@ConditionalOnProperty(name = "open-library.storage.replace-url-handler-config.pointcut-expression")
@AutoConfigureAfter(StorageAutoConfigure.class)
@Configuration
public class StorageReplaceURLHandlerConfigure {
    
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
        pointcut.setExpression(storageConfig.getReplaceUrlHandlerConfig().getPointcutExpression());
        return new DefaultPointcutAdvisor(pointcut, new ReplaceURLHandler(requestReplaceURLHandlers, responseReplaceUrlHandlers));
    }
}
