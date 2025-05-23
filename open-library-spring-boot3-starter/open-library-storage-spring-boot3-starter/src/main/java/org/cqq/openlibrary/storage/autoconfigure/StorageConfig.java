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
    
    private String replaceUrlHandler;
    
    private LocalStorageConfig local;
}
