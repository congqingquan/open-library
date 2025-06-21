package org.cqq.openlibrary.mybatis.mybatisplus.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import org.cqq.openlibrary.common.util.CollectionUtils;

import java.util.List;
 
/**
 * MP tenant handler
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class TenantHandler implements TenantLineHandler {
 
    private final List<String> ignoreTables;
    
    private final String tenantIdColumn;
    
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
        return tenantIdColumn;
    }
    
    @Override
    public boolean ignoreTable(String tableName) {
        //忽略指定表对租户数据的过滤
        return CollectionUtils.isNotEmpty(ignoreTables) && ignoreTables.contains(tableName);
    }
}