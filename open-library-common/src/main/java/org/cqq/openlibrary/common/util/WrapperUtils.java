package org.cqq.openlibrary.common.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class WrapperUtils {

    public static UpdateWrapper updateWrapper(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        for (Field field : fields) {
            // 设置访问权限，因为有些字段可能是私有的
            field.setAccessible(true);

            // 获取字段值
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if ("id".equals(field.getName())) {
                if (fieldValue == null) {
                    throw new RuntimeException("参数Id不能为空!");
                }
                updateWrapper.eq("id", fieldValue);
            }

            // 检查字段值是否为 null
            if (fieldValue != null) {
                String name = field.getName();
                if (!"id".equals(name)) {
                    TableField annotation = field.getAnnotation(TableField.class);
                    String value = annotation.value();
                    if (value.isEmpty()) {
                        throw new RuntimeException("内部异常,请联系管理员!");
                    }
                    updateWrapper.set(value, fieldValue);
                }
            }
        }
        return updateWrapper;
    }


    public static QueryWrapper queryWrapper(Object obj, String... excludeFields) {
        List<String> excludeFieldList = Arrays.asList(excludeFields);
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        QueryWrapper queryWrapper = new QueryWrapper<>();
        for (Field field : fields) {
            // 设置访问权限，因为有些字段可能是私有的
            field.setAccessible(true);

            // 如果被排除,则说明调用者不希望这个字段进行条件拼接
            if (excludeFieldList.contains(field.getName())) {
                continue;
            }

            // 获取字段值
            Object fieldValue = null;
            try {
                fieldValue = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            // 检查字段值是否为 null
            if (fieldValue != null) {
                TableField annotation = field.getAnnotation(TableField.class);
                if (annotation != null) {
                    String value = annotation.value();
                    if (value.isEmpty()) {
                        throw new RuntimeException("内部异常,请联系管理员!");
                    }
                    queryWrapper.eq(value, fieldValue);
                } else {
                    queryWrapper.eq("id", fieldValue);
                }

            }
        }
        queryWrapper.eq("deleted",0);
        return queryWrapper;
    }
}
