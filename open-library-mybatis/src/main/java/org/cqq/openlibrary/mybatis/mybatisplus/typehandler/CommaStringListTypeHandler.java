package org.cqq.openlibrary.mybatis.mybatisplus.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Type handler: List<Object> <-> String(e1),String(e2),String(e3)
 *
 * <p>
 * Need to configuration in application.yaml:
 * mybatis-plus:
 *  type-handlers-package: org.cqq.openlibrary.common.persistent.mybatis.typehandler
 * <p>
 *
 * @author Qingquan
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class CommaStringListTypeHandler implements TypeHandler<List<Object>> {

    private static final String COMMA = ",";

    @Override
    public void setParameter(PreparedStatement ps, int i, List<Object> strings, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(COMMA, strings.stream().map(Object::toString).toList()));
    }

    @Override
    public List<Object> getResult(ResultSet rs, String columnName) throws SQLException {
        return getResult(rs.getString(columnName));
    }

    @Override
    public List<Object> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getResult(rs.getString(columnIndex));
    }

    @Override
    public List<Object> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getResult(cs.getString(columnIndex));
    }

    private List<Object> getResult(Object value) {
        if (value == null) {
            return null;
        }
        return List.of(value.toString().split(COMMA));
    }
}
