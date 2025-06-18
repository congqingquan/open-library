package org.cqq.openlibrary.storage.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 存储配置
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {
    
    private String active;
    
    private LocalStorageConfig local;
    
    private ReplaceURLHandler replaceUrlHandler;
    
    @Data
    public static class ReplaceURLHandler {
        private Boolean enable;
        private String pointcutExpression;
    }
}

