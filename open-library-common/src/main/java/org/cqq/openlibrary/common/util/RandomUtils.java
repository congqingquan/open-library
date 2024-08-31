package org.cqq.openlibrary.common.util;

/**
 * Random utils
 *
 * @author CongQingquan
 */
public class RandomUtils {
    
    private RandomUtils() {}
    
    // Range > [0, 1)
    public static double randomScaledDecimal(int scale) {
        double pow = Math.pow(10, scale);
        return (double) ((long) (Math.random() * pow)) / pow;
    }
}