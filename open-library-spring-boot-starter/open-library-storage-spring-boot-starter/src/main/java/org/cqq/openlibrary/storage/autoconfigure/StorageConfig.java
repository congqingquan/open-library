package org.cqq.openlibrary.storage.autoconfigure;

import lombok.Data;
import org.cqq.openlibrary.storage.enums.StorageTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Storage config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.storage")
public class StorageConfig {
    
    private StorageTypeEnum active;
    
    private LocalStorageConfig localStorageConfig;
    
    private ReplaceURLHandlerConfig replaceUrlHandlerConfig;
    
    // ========================================== Local ==========================================
    
    @Data
    public static class LocalStorageConfig {
        
        private String rootDir;
        
        private String writeDir;
        
        private Server server;
        
        @Data
        public static class Server {
            
            private String protocol;
            
            private String host;
            
            private Integer port;
        }
    }
    
    // ========================================== ReplaceURLHandler ==========================================
    
    @Data
    public static class ReplaceURLHandlerConfig {
        private String pointcutExpression;
    }
}

