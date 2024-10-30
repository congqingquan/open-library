package org.cqq.openlibrary.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Function;

/**
 * 大浮点工具类
 *
 * @author Qingquan
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static BigDecimal min(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.handleNull(b1, b2, Function.identity(), BigDecimal::min);
    }

    public static BigDecimal max(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.handleNull(b1, b2, Function.identity(), BigDecimal::max);
    }

    public static boolean gt(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.compareTo(b1, b2, (a, b) -> a.compareTo(b) > 0);
    }

    public static boolean gte(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.compareTo(b1, b2, (a, b) -> a.compareTo(b) >= 0);
    }

    public static boolean equals(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.compareTo(b1, b2, (a, b) -> a.compareTo(b) == 0);
    }

    public static boolean lt(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.compareTo(b1, b2, (a, b) -> a.compareTo(b) < 0);
    }

    public static boolean lte(BigDecimal b1, BigDecimal b2) {
        return ObjectUtils.compareTo(b1, b2, (a, b) -> a.compareTo(b) <= 0);
    }

    // 简化：去除尾部0后转换为普通表示法的浮点数
    public static BigDecimal simplify(BigDecimal decimal) {
        return Optional.ofNullable(decimal).map(d -> new BigDecimal(d.stripTrailingZeros().toPlainString())).orElse(null);
    }

    // 整数分转浮点
    public static BigDecimal cents2Decimal(Integer cents) {
        return cents2Decimal((long) cents);
    }

    public static BigDecimal cents2Decimal(Long cents) {
        return new BigDecimal(cents).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }
}