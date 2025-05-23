package org.cqq.openlibrary.common.func;

import org.cqq.openlibrary.common.util.BigDecimalUtils;
import org.cqq.openlibrary.common.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Function expression
 *
 * @author Qingquan
 */
public class FuncExpr {

    private FuncExpr() {}

    public static <T extends Comparable<T>> Predicate<T> between(T start, T end) {
        return elment -> elment.compareTo(start) >= 0 && elment.compareTo(end) <= 0;
    }
    
    // ============================================ Predicate ============================================
    
    public static <T> Predicate<T> isNull(Function<? super T, ?> extractor) {
        return t -> Objects.isNull(extractor.apply(t));
    }
    
    public static <T> Predicate<T> nonNull(Function<? super T, ?> extractor) {
        return t -> Objects.nonNull(extractor.apply(t));
    }
    
    public static <T, R extends CharSequence> Predicate<T> isBlank(Function<? super T, ? extends R> extractor) {
        return t -> StringUtils.isBlank(extractor.apply(t));
    }
    
    public static <T, R extends CharSequence> Predicate<T> isNotBlank(Function<? super T, ? extends R> extractor) {
        return t -> StringUtils.isNotBlank(extractor.apply(t));
    }
    
    public static <T, R extends Number> Predicate<T> isZero(Function<? super T, R> extractor) {
        return t -> {
            R number = extractor.apply(t);
            if (number == null) {
                return false;
            }
            if (number instanceof Byte) {
                return number.byteValue() == 0;
            } else if (number instanceof Short) {
                return number.shortValue() == 0;
            } else if (number instanceof Integer) {
                return number.intValue() == 0;
            } else if (number instanceof Long) {
                return number.longValue() == 0;
            } else if (number instanceof Float) {
                return number.floatValue() == 0;
            } else if (number instanceof Double) {
                return number.doubleValue() == 0;
            } else if (number instanceof BigDecimal decimal) {
                return BigDecimalUtils.equals(decimal, BigDecimal.ZERO);
            } else {
                throw new IllegalArgumentException("Unsupported number type");
            }
        };
    }
}