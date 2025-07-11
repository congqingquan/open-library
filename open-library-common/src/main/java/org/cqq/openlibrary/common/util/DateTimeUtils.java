package org.cqq.openlibrary.common.util;

import org.cqq.openlibrary.common.constants.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Datetime utils
 *
 * @author Qingquan
 */
public class DateTimeUtils {
    
    private DateTimeUtils() {
    }
    
    public static Boolean isValid(LocalDateTime dateTime) {
        if (dateTime == null) {
            return false;
        }
        return !Constants.DATE_TIME_1970_01_01_00_00_00.equals(dateTime);
    }
    
    public static String format(LocalDateTime dateTime) {
        return format(dateTime, Constants.yyyy_MM_dd_HH_mm_ss);
    }
    
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            throw new IllegalArgumentException("DateTime is null");
        }
        return DateTimeFormatter.ofPattern(pattern).format(dateTime);
    }
}
