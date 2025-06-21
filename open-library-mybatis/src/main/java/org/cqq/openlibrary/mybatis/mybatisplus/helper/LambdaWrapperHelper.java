package org.cqq.openlibrary.mybatis.mybatisplus.helper;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import org.cqq.openlibrary.common.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Lambda wrapper helper
 *
 * @author Qingquan
 */
public class LambdaWrapperHelper {
    
    private LambdaWrapperHelper() {}
    
    @Data
    public static class Condition<T> {
        
        private Boolean condition;
        
        private SFunction<T, ?> field;
        
        private Object value;
        
        private SqlKeyword keyword;
        
        public Condition(SFunction<T, ?> field, Object value, SqlKeyword keyword) {
            this.condition  = true;
            this.field = field;
            this.value = value;
            this.keyword = keyword;
        }
        
        public Condition(Boolean condition, SFunction<T, ?> field, Object value, SqlKeyword keyword) {
            this.condition = condition;
            this.field = field;
            this.value = value;
            this.keyword = keyword;
        }
    }
    
    @SafeVarargs
    public static <T> LambdaQueryWrapper<T> createQuery(Condition<T>... conditions) {
        return createQuery(Arrays.asList(conditions));
    }
    
    public static <T> LambdaQueryWrapper<T> createQuery(Collection<Condition<T>> conditions) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        appendCondition(wrapper, conditions);
        return wrapper;
    }
    
    public static <T, R extends AbstractLambdaWrapper<T, ?>> R appendCondition(R wrapper, Collection<Condition<T>> conditions) {
        if (wrapper == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(conditions)) {
            return wrapper;
        }
        for (Condition<T> condition : conditions) {
            SFunction<T, ?> field = condition.getField();
            if (field == null) {
                continue;
            }
            Boolean cb = condition.getCondition();
            Object value = condition.getValue();
            
            switch (condition.getKeyword()) {
                case EQ -> wrapper.eq(cb, field, value);
                case IN -> wrapper.in(cb, field, (Collection<?>) value);
                case NOT_IN -> wrapper.notIn(cb, field, (Collection<?>) value);
            }
        }
        return wrapper;
    }
}