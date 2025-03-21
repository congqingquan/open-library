package org.cqq.openlibrary.common.persistent.mybatis.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import org.cqq.openlibrary.common.util.CollectionUtils;

import java.util.List;
 
/**
 * MP 租户拦截器
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class TenantHandler implements TenantLineHandler {
 
    private final TenantConfig config;
    
    @Override
    public Expression getTenantId() {
        Long tenantId = TenantHolder.getTenantId();
        if (tenantId == null) {
            return new IsNullExpression();
        }
        return new LongValue(tenantId);
    }
    
    @Override
    public String getTenantIdColumn() {
        return config.getColumn();
    }
    
    @Override
    public boolean ignoreTable(String tableName) {
        //忽略指定表对租户数据的过滤
        List<String> ignoreTables = config.getIgnoreTables();
        return CollectionUtils.isNotEmpty(ignoreTables) && ignoreTables.contains(tableName);
    }
}