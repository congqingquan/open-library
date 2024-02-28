package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Enum utils
 *
 * @author Qingquan.Cong
 */
public class EnumUtils {

    private EnumUtils() {
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String name) {
        try {
            Enum.valueOf(enumClass, name);
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    public static <E extends Enum<E>> List<E> getEnumConstants(Class<E> enumClass) {
        return enumClass == null ? new ArrayList<>() : CollectionUtils.newArrayList(enumClass.getEnumConstants());
    }

    /**
     * 匹配枚举实例
     * @param enumClass 枚举的类对象
     * @param predicate <Enum, MatchValue>
     * @param matchValue 匹配值
     */
    public static <M, E extends Enum<E>> Optional<E> match(Class<E> enumClass, BiPredicate<E, M> predicate, M matchValue) {
        return match(enumClass.getEnumConstants(), predicate, matchValue);
    }

    /**
     * 匹配枚举实例
     * @param enums 需要匹配的枚举实例
     * @param predicate <Enum, MatchValue>
     * @param matchValue 匹配值
     */
    public static <M, E extends Enum<E>> Optional<E> match(E[] enums, BiPredicate<E, M> predicate, M matchValue) {
        for (E enumConstant : enums) {
            if (predicate.test(enumConstant, matchValue)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }

    /**
     * 匹配枚举实例
     * @param enumClass 枚举的类对象
     * @param getEnumCompareField 获取 Enum equal 字段
     * @param matchValue 匹配值
     */
    public static <M, E extends Enum<E>> Optional<E> equalMatch(Class<E> enumClass, Function<E, M> getEnumCompareField, M matchValue) {
        return equalMatch(enumClass.getEnumConstants(), getEnumCompareField, matchValue);
    }

    /**
     * 匹配枚举实例
     * @param enums 需要匹配的枚举实例
     * @param getEnumCompareField 获取 Enum equal 字段
     * @param matchValue 匹配值
     */
    public static <M, E extends Enum<E>> Optional<E> equalMatch(E[] enums, Function<E, M> getEnumCompareField, M matchValue) {
        for (E enumConstant : enums) {
            if (getEnumCompareField.apply(enumConstant).equals(matchValue)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }
}