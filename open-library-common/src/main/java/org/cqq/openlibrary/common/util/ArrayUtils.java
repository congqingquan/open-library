package org.cqq.openlibrary.common.util;

import java.lang.reflect.Array;

/**
 * Array utils
 *
 * @author Qingquan.Cong
 */
public class ArrayUtils {

    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    // ================================================================================

    public static boolean isEmpty(final boolean[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final byte[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final char[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final short[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final int[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final long[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final float[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final double[] array) {
        return getLength(array) == 0;
    }

    public static boolean isEmpty(final Object[] array) {
        return getLength(array) == 0;
    }

    // ================================================================================

    public static boolean isNotEmpty(final boolean[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final byte[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final short[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final char[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final int[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final long[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final float[] array) {
        return !isEmpty(array);
    }

    public static boolean isNotEmpty(final double[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }
}