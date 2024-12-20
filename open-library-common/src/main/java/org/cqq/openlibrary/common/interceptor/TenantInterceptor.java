package org.cqq.openlibrary.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.cqq.openlibrary.common.persistent.mybatis.tenant.TenantConfig;
import org.cqq.openlibrary.common.persistent.mybatis.tenant.TenantHolder;
import org.cqq.openlibrary.common.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 租户拦截器
 *
 * @author Qingquan
 */
@Data
@AllArgsConstructor
public class TenantInterceptor  implements HandlerInterceptor {
    
    private final TenantConfig tenantConfig;
    
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String tenantId = request.getHeader(tenantConfig.getHttpHeaderTenantId());
        if (StringUtils.isNotBlank(tenantId)) {
            TenantHolder.setTenantId(Long.valueOf(tenantId));
        }
        return true;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        TenantHolder.clearTenantId();
    }
}