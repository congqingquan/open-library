package org.cqq.openlibrary.common.presistent.mybatis.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.cqq.openlibrary.common.util.EnumUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Override default enum type handler(return null when cannot find the enum constant)
 * The original default enum type handler: org.apache.ibatis.type.EnumTypeHandler
 * @see org.apache.ibatis.type.EnumTypeHandler
 *
 * <p>
 * Need to configuration in application.yml:
 * mybatis-plus:
 *  configuration:
 *   default-enum-type-handler: org.cqq.openlibrary.common.presistent.mybatis.typehandler.EnumTypeHandler
 * </p>
 *
 * @author CongQingquan
 */
@MappedTypes(Enum.class)
public class EnumTypeHandler<E extends Enum<E>> implements TypeHandler<E> {
    
    private final Class<E> type;
    
    public EnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }
    
    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public E getResult(ResultSet rs, String columnName) throws SQLException {
        return EnumUtils.equalMatchByName(type, rs.getString(columnName)).orElse(null);
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        return EnumUtils.equalMatchByName(type, rs.getString(columnIndex)).orElse(null);
    }

    @Override
    public E getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EnumUtils.equalMatchByName(type, cs.getString(columnIndex)).orElse(null);
    }
}
