package org.cqq.openlibrary.common.component.checkfield;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modified field checker
 *
 * @author Qingquan.Cong
 */
@Slf4j
public abstract class ModifiedFieldChecker {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected Class<? extends Annotation> flagAnnotationClass;

    protected Class<? extends Annotation> ignoreAnnotationClass;

    protected ModifiedFieldChecker(Class<? extends Annotation> flagAnnotationClass,
                                   Class<? extends Annotation> ignoreAnnotationClass) {
        if (flagAnnotationClass == null) {
            throw new IllegalArgumentException("Flag annotation class can not be null");
        }
        if (ignoreAnnotationClass == null) {
            throw new IllegalArgumentException("Ignore annotation class can not be null");
        }
        this.flagAnnotationClass = flagAnnotationClass;
        this.ignoreAnnotationClass = ignoreAnnotationClass;
    }

    /**
     * 比较实例字段值是否相同
     * <p>
     * 实例类型字段仅有两种判断：
     * 1. object equals
     * 2. array equals
     *
     * @param sourceFieldVal  源字段值
     * @param compareFieldVal 比较字段值
     * @return 比较结果
     */
    protected boolean compareFieldValue(Object sourceFieldVal, Object compareFieldVal) {
        if (sourceFieldVal == compareFieldVal) {
            return true;
        }
        if (sourceFieldVal == null || compareFieldVal == null) {
            return false;
        }
        // 提前判断 collection & map, 目的在于提供两个自定义判断数据容器是否相等的插口(数据容器已经重写 equals 方法)。
        // collection equals
        if (sourceFieldVal instanceof Collection && compareFieldVal instanceof Collection) {
            return compareCollection((Collection<?>) sourceFieldVal, (Collection<?>) compareFieldVal);
        }
        // map equals
        if (sourceFieldVal instanceof Map && compareFieldVal instanceof Map) {
            return compareMap((Map<?, ?>) sourceFieldVal, (Map<?, ?>) compareFieldVal);
        }
        // object equals
        if (sourceFieldVal.equals(compareFieldVal)) {
            return true;
        }
        // array equals
        if (sourceFieldVal.getClass().isArray() && compareFieldVal.getClass().isArray()) {
            return compareArray(sourceFieldVal, compareFieldVal);
        }
        return false;
    }

    /**
     * 比较实例
     *
     * @param sourceFieldVal  源字段值
     * @param compareFieldVal 比较字段值
     * @return 比较结果
     */
    protected boolean compareObject(Object sourceFieldVal, Object compareFieldVal) {
        if (sourceFieldVal == compareFieldVal) {
            return true;
        }
        if (sourceFieldVal == null || compareFieldVal == null) {
            return false;
        }
        return sourceFieldVal.equals(compareFieldVal);
    }

    /**
     * 比较数组
     *
     * @param sourceFieldVal  源数组字段值
     * @param compareFieldVal 比较数组字段值
     * @return 比较结果
     */
    protected boolean compareArray(Object sourceFieldVal, Object compareFieldVal) {
        // 包装类型数组也属于 Object[], 每个元素通过 equals 比较
        if (sourceFieldVal instanceof Object[] && compareFieldVal instanceof Object[]) {
            return Arrays.equals((Object[]) sourceFieldVal, (Object[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof boolean[] && compareFieldVal instanceof boolean[]) {
            return Arrays.equals((boolean[]) sourceFieldVal, (boolean[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof char[] && compareFieldVal instanceof char[]) {
            return Arrays.equals((char[]) sourceFieldVal, (char[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof byte[] && compareFieldVal instanceof byte[]) {
            return Arrays.equals((byte[]) sourceFieldVal, (byte[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof short[] && compareFieldVal instanceof short[]) {
            return Arrays.equals((short[]) sourceFieldVal, (short[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof int[] && compareFieldVal instanceof int[]) {
            return Arrays.equals((int[]) sourceFieldVal, (int[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof long[] && compareFieldVal instanceof long[]) {
            return Arrays.equals((long[]) sourceFieldVal, (long[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof float[] && compareFieldVal instanceof float[]) {
            return Arrays.equals((float[]) sourceFieldVal, (float[]) compareFieldVal);
        }
        if (sourceFieldVal instanceof double[] && compareFieldVal instanceof double[]) {
            return Arrays.equals((double[]) sourceFieldVal, (double[]) compareFieldVal);
        }
        return false;
    }

    /**
     * 比较集合
     *
     * @param sourceFieldVal  源集合字段值
     * @param compareFieldVal 比较集合字段值
     * @return 比较结果
     */
    protected abstract boolean compareCollection(Collection<?> sourceFieldVal, Collection<?> compareFieldVal);

    /**
     * 比较映射表
     *
     * @param sourceFieldVal  源映射表字段值
     * @param compareFieldVal 比较映射表字段值
     * @return 比较结果
     */
    protected abstract boolean compareMap(Map<?, ?> sourceFieldVal, Map<?, ?> compareFieldVal);

    /**
     * 获取标记了指定注解的字段
     *
     * @param obj 搜索实例
     * @return 标记了指定注解的字段列表
     */
    protected List<Field> searchMarkedAnnotationFields(Object obj) {
        if (null == obj) {
            return new ArrayList<>();
        }
        List<Field> result = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 1. 是否标记了忽略注解
            if (field.isAnnotationPresent(ignoreAnnotationClass)) {
                continue;
            }
            // 2. 字段或类上是否存有标记注解
            if (field.isAnnotationPresent(flagAnnotationClass) || obj.getClass().isAnnotationPresent(flagAnnotationClass)) {
                result.add(field);
            }
        }
        return result;
    }

    /**
     * 搜寻不同值字段
     *
     * @param sourceObj  源实例
     * @param compareObj 比较实例
     * @return ResultMap {field of source object, field of compared object}
     */
    protected Map<Field, Field> searchDifferentField(Object sourceObj, Object compareObj) {
        Map<Field, Field> collector = new LinkedHashMap<>();
        if (sourceObj == null || compareObj == null) {
            return collector;
        }
        List<Field> fields = searchMarkedAnnotationFields(sourceObj);
        try {
            Class<?> compareObjClass = compareObj.getClass();
            for (Field sourceField : fields) {
                // 判断是否一致：
                // 1. 名称相同
                // 2. 值相同
                Field compareField;
                try {
                    compareField = compareObjClass.getDeclaredField(sourceField.getName());
                } catch (NoSuchFieldException e) {
                    continue;
                }
                compareField.setAccessible(Boolean.TRUE);
                sourceField.setAccessible(Boolean.TRUE);
                Object sourceFieldValue = sourceField.get(sourceObj);
                Object compareFieldValue = compareField.get(compareObj);
                if (!compareFieldValue(sourceFieldValue, compareFieldValue)) {
                    collector.put(sourceField, compareField);
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Search different field error", e);
        }
        return collector;
    }

    /**
     * 后置处理
     *
     * @param sourceObj  源实例
     * @param compareObj 比较实例
     * @param resultMap  ResultMap {field of source object, field of compared object}
     */
    protected void postProcess(Object sourceObj, Object compareObj, Map<Field, Field> resultMap) {
        // Do nothing
    }

    /**
     * 执行
     *
     * @param sourceObj  源实例
     * @param compareObj 比较实例
     * @return ResultMap {field of source object, field of compared object}
     */
    public Map<Field, Field> process(Object sourceObj, Object compareObj) {
        Map<Field, Field> resultMap = searchDifferentField(sourceObj, compareObj);
        postProcess(sourceObj, compareObj, resultMap);
        return resultMap;
    }
}