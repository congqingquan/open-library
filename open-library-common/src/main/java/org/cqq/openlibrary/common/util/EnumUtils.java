package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Enum utils
 *
 * @author Qingquan
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
    
    @SuppressWarnings("unchecked")
    public static Class<Enum<?>> toWildcardEnumClass(Class<?> rawEnumClass) {
        if (!rawEnumClass.isEnum()) {
            throw new IllegalArgumentException("Class must be an enum");
        }
        return (Class<Enum<?>>) rawEnumClass;
    }
    
    public static <E extends Enum<E>> List<E> getEnumConstants(Class<E> enumClass) {
        return enumClass == null ? new ArrayList<>() : CollectionUtils.newArrayList(enumClass.getEnumConstants());
    }


    // ============================ Match enum member ============================
    
    public static <M, E extends Enum<E>> Optional<E> match(Class<E> enumClass, BiPredicate<E, M> predicate, M matchValue) {
        return match(enumClass.getEnumConstants(), predicate, matchValue);
    }

    public static <M, E extends Enum<E>> Optional<E> match(E[] enums, BiPredicate<E, M> predicate, M matchValue) {
        for (E enumConstant : enums) {
            if (predicate.test(enumConstant, matchValue)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }
    
    public static <M, E extends Enum<E>> Optional<E> equalMatch(Class<E> enumClass, Function<E, M> getEnumCompareField, M matchValue) {
        return equalMatch(enumClass.getEnumConstants(), getEnumCompareField, matchValue);
    }
    
    public static <M, E extends Enum<E>> Optional<E> equalMatch(E[] enums, Function<E, M> getEnumCompareField, M matchValue) {
        for (E enumConstant : enums) {
            if (getEnumCompareField.apply(enumConstant).equals(matchValue)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }
    
    public static <E extends Enum<?>> Optional<E> equalMatchByName(Class<E> enumClass, String name) {
        return equalMatchByName(enumClass.getEnumConstants(), name);
    }
    
    public static <M, E extends Enum<?>> Optional<E> equalMatchByName(E[] enums, String name) {
        for (E enumConstant : enums) {
            if (enumConstant.name().equals(name)) {
                return Optional.of(enumConstant);
            }
        }
        return Optional.empty();
    }
}