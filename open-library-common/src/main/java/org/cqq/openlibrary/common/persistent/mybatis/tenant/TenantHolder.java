package org.cqq.openlibrary.common.persistent.mybatis.tenant;

/**
 * 租户上下文 Holder
 *
 * @author Qingquan
 */
public class TenantHolder {
    
    private static final ThreadLocal<Long> TENANT_ID = ThreadLocal.withInitial(() -> null);
    
    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }
 
    public static Long getTenantId() {
        return TENANT_ID.get();
    }
    
    public static void clearTenantId() {
        TENANT_ID.remove();
    }
}