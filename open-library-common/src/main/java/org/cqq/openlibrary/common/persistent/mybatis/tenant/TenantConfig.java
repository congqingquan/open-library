package org.cqq.openlibrary.common.persistent.mybatis.tenant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 租户配置
 *
 * @author Qingquan
 */
@Data
public class TenantConfig {
    
    @Value("${tenant.enable:false}")
    private Boolean enable;
    
    @Value("${tenant.column:tenant_id}")
    private String column;
    
    @Value("${tenant.ignore-tables:}")
    private List<String> ignoreTables;
    
    @Value("${tenant.http-header.tenant-id:TenantId}")
    private String httpHeaderTenantId;
}
