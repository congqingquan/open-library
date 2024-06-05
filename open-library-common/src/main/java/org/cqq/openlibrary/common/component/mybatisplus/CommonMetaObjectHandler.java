package org.cqq.openlibrary.common.component.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;
import java.util.Objects;

/**
 * 1. 注入配置中指定的字段 同时 必须在 PO 中的同字段上声明: @TableField(fill = FieldFill.XXX) 才会生效
 * 2. 被指定了 @TableField(fill = FieldFill.XXX) 注解的字段，如果数据库设置了 not null，那么程序中就必须保证一定有可以 fill 的值。
 * 因为 MP 处理需要注入值的字段，无论给出的 fill 值是否为空，都会在最终的 SQL 中拼出该字段。而非若给出的 fill 的值为空，就不操作该字段。
 * 那么当数据库中设置了 not null，但是程序中没有给出 fill 的值，必定抛出数据库异常。
 * 即：
 * not null -> 必须有 fill 值
 * default null -> 不必须有 fill 值
 *
 * @author Qingquan.Cong
 */
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {

    private final List<FieldFillConfig> insertFieldConfig;

    private final List<FieldFillConfig> updateFieldConfig;

    public CommonMetaObjectHandler(List<FieldFillConfig> insertFieldConfig, List<FieldFillConfig> updateFieldConfig) {
        this.insertFieldConfig = insertFieldConfig;
        this.updateFieldConfig = updateFieldConfig;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        for (FieldFillConfig fieldFillConfig : insertFieldConfig) {
            try {
                String fieldName = fieldFillConfig.getFieldName();
                Object fillValue = fieldFillConfig.getFillValue().get();
                if (Objects.isNull(fillValue)) {
                    log.warn("The fill value is null. Field name [{}]", fieldName);
                }
                boolean predicate = fieldFillConfig.getFillPredicate().test(this, metaObject);
                if (predicate) {
                    setInsertFieldValByName(fieldName, fillValue, metaObject);
                }
            } catch (Exception exception) {
                log.error("Handle field fill config error. Field name: {}", fieldFillConfig.getFieldName(), exception);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        for (FieldFillConfig fieldFillConfig : updateFieldConfig) {
            try {
                String fieldName = fieldFillConfig.getFieldName();
                Object fillValue = fieldFillConfig.getFillValue().get();
                if (Objects.isNull(fillValue)) {
                    log.warn("The fill value is null. Field name [{}]", fieldName);
                }
                boolean predicate = fieldFillConfig.getFillPredicate().test(this, metaObject);
                if (predicate) {
                    setUpdateFieldValByName(fieldName, fillValue, metaObject);
                }
            } catch (Exception exception) {
                log.error("Handle field fill config error. Field name: {}", fieldFillConfig.getFieldName(), exception);
            }
        }
    }
}