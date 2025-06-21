package org.cqq.openlibrary.mybatis.mybatisplus.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Mybatis-plus config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.mybatis.mybatis-plus")
public class MybatisPlusConfig {
    
    private TenantConfig tenantConfig;
    
    @Data
    public static class TenantConfig {
        
        private String tenantIdColumn;
        
        private List<String> ignoreTables;
        
        private String httpHeaderTenantId;
    }
}
