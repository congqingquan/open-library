package org.cqq.openlibrary.storage.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.storage.Storage;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.controlleradvice.DefaultReplaceURLResponseHandler;
import org.cqq.openlibrary.storage.local.LocalStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    
    @ConditionalOnProperty(name = "storage.replace-url-handler", havingValue = "default")
    @RestControllerAdvice
    public static class InjectDefaultReplaceURLResponseHandler extends DefaultReplaceURLResponseHandler {
        public InjectDefaultReplaceURLResponseHandler(StorageProvider provider) {
            super(provider);
        }
    }
}
