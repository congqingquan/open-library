package org.cqq.openlibrary.common.func;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Function expression
 *
 * @author Qingquan.Cong
 */
public class FuncExpr {

    private FuncExpr() {}

    public static <T extends Comparable<T>> Predicate<T> between(T start, T end) {
        return elment -> elment.compareTo(start) >= 0 && elment.compareTo(end) <= 0;
    }
    
    public static <T> Predicate<T> isNull(Function<? super T, ?> extractor) {
        return t -> Objects.isNull(extractor.apply(t));
    }
    
    public static <T> Predicate<T> nonNull(Function<? super T, ?> extractor) {
        return t -> Objects.nonNull(extractor.apply(t));
    }
}