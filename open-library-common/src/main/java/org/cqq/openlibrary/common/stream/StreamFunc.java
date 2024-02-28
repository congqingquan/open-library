package org.cqq.openlibrary.common.stream;

import java.util.function.Predicate;

/**
 * Stream functions
 *
 * @author Qingquan.Cong
 */
public class StreamFunc {

    private StreamFunc() {}

    public static <T extends Comparable<T>> Predicate<T> between(T start, T end) {
        return elment -> elment.compareTo(start) >= 0 && elment.compareTo(end) <= 0;
    }
}