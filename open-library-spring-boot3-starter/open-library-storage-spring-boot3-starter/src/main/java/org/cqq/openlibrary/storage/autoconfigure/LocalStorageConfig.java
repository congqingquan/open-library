package org.cqq.openlibrary.storage.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地存储配置
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "storage.local")
public class LocalStorageConfig {
    
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
