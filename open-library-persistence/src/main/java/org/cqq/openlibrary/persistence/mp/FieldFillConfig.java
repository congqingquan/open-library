package org.cqq.openlibrary.persistence.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Created by QQ.Cong on 2023-06-06 / 19:31:12
 *
 * @Description 字段填充配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldFillConfig {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 填充值
     */
    private Supplier<?> fillValue;

    /**
     * 字段断言（用于相同字段对于不同实例的不同处理）
     */
    private BiPredicate<MetaObjectHandler, MetaObject> fillPredicate;

    /**
     * 创建字段值为空断言
     */
    public static BiPredicate<MetaObjectHandler, MetaObject> createIsNullPredicate(String fieldName) {
        return (metaObjectHandler, metaObject) -> Objects.isNull(metaObjectHandler.getFieldValByName(fieldName, metaObject));
    }
}