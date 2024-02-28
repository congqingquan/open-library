package org.cqq.openlibrary.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 大浮点工具类
 *
 * @author Qingquan.Cong
 */
public class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static BigDecimal min(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.min(compareBigDecimal);
    }

    public static BigDecimal max(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.max(compareBigDecimal);
    }

    public static boolean gt(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.compareTo(compareBigDecimal) > 0;
    }

    public static boolean gte(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.compareTo(compareBigDecimal) >= 0;
    }

    public static boolean equal(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.compareTo(compareBigDecimal) == 0;
    }

    public static boolean lt(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.compareTo(compareBigDecimal) < 0;
    }

    public static boolean lte(BigDecimal bigDecimal, BigDecimal compareBigDecimal) {
        return bigDecimal.compareTo(compareBigDecimal) <= 0;
    }

    public static boolean equalToZero(BigDecimal bigDecimal) {
        return equal(BigDecimal.ZERO, bigDecimal);
    }

    /**
     * 去除尾部0后转换为普通表示法的浮点数
     */
    public static BigDecimal toPlainBigDecimalAfterStripZeros(BigDecimal bigDecimal) {
        return bigDecimal == null ? null
                : new BigDecimal(bigDecimal.stripTrailingZeros().toPlainString());
    }

    /**
     * 整数分转浮点
     */
    public static BigDecimal cents2Decimal(Integer cents) {
        return cents2Decimal((long) cents);
    }

    /**
     * 整数分转浮点
     */
    public static BigDecimal cents2Decimal(Long cents) {
        return new BigDecimal(cents).divide(new BigDecimal(100), 2, RoundingMode.DOWN);
    }
}