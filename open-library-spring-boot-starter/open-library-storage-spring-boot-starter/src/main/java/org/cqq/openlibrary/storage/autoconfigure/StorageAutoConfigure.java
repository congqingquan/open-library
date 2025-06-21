package org.cqq.openlibrary.storage.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.storage.Storage;
import org.cqq.openlibrary.storage.StorageProvider;
import org.cqq.openlibrary.storage.local.LocalStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * Storage auto configure
 *
 * @author Qingquan
 */
@Slf4j
@ConditionalOnProperty(name = "open-library.storage.active")
@EnableConfigurationProperties(StorageConfig.class)
@Import(StorageReplaceURLHandlerConfigure.class)
@Configuration
public class StorageAutoConfigure {
    
    @Bean
    public Storage.Manager storageManager(List<Storage> storages) {
        return new Storage.Manager(storages);
    }

    @Bean
    public StorageProvider storageProvider(StorageConfig config,
                                           Storage.Manager manager) {
        return () -> manager.get(config.getActive());
    }
    
    
    // ======================================== Local ========================================
    
    @ConditionalOnProperty(name = "open-library.storage.active", havingValue = "LOCAL")
    @Bean
    public Storage localStorage(StorageConfig config) {
        StorageConfig.LocalStorageConfig localStorageConfig = config.getLocalStorageConfig();
        StorageConfig.LocalStorageConfig.Server server = localStorageConfig.getServer();
        return new LocalStorage(
                localStorageConfig.getRootDir(),
                localStorageConfig.getWriteDir(),
                server.getProtocol(),
                server.getHost(),
                server.getPort()
        );
    }
}
