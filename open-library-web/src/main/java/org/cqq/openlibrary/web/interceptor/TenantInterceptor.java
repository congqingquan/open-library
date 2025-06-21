package org.cqq.openlibrary.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.cqq.openlibrary.common.util.StringUtils;
import org.cqq.openlibrary.mybatis.mybatisplus.tenant.TenantHolder;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Tenant interceptor
 *
 * @author Qingquan
 */
@Data
@AllArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {
    
    private final String tenantIdHttpHeader;
    
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        String tenantId = request.getHeader(tenantIdHttpHeader);
        if (StringUtils.isNotBlank(tenantId)) {
            TenantHolder.setTenantId(Long.valueOf(tenantId));
        }
        return true;
    }
    
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request,
                                @NotNull HttpServletResponse response,
                                @NotNull Object handler,
                                Exception ex) {
        TenantHolder.clearTenantId();
    }
}