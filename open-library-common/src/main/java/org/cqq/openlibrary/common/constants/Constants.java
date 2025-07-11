package org.cqq.openlibrary.common.constants;

import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * Constants
 *
 * @author Qingquan
 */
public class Constants {

    // ======================================== 时间格式 ========================================
    public static final String yyyy = "yyyy";
    public static final String MM = "MM";
    public static final String dd = "dd";
    public static final String HH = "HH";
    public static final String mm = "mm";
    public static final String ss = "ss";
    public static final String SSS = "SSS";
    public static final String HH_mm_ss = HH + ":" + mm + ":" + ss;
    public static final String yyyy_MM_dd = yyyy + "-" + MM + "-" + dd;
    public static final String yyyy_MM_dd_HH = yyyy_MM_dd + " " + HH;
    public static final String yyyy_MM_dd_HH_mm = yyyy_MM_dd_HH + ":" + mm;
    public static final String yyyy_MM_dd_HH_mm_ss = yyyy_MM_dd_HH_mm + ":" + ss;
    public static final String yyyy_MM_dd_HH_mm_ss_SSS = yyyy_MM_dd_HH_mm_ss + ":" + SSS;
    
    public static final LocalDateTime DATE_TIME_1970_01_01_00_00_00 = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
    
    public static final String DATE_TIME_1970_01_01_00_00_00_STRING = "1970-01-01 00:00:00";
    
    // ======================================== Symbol ========================================
    
    public static final String COMMA = ",";
    
    public static final String DOT = ".";
    
    // ======================================== Time zone ========================================
    
    /**
     * GMT 时区: 格林威治标准时间的英文缩写(Greenwich Mean Time 格林尼治标准时间),是世界标准时间.
     *
     * GMT+8 是格林威治时间+8小时.中国所在时区就是GMT+8.
     */
    public static final String TIME_ZONE_GMT_8 = "GMT+8";
    
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE_GMT_8);
    
    // ======================================== Biz symbol ========================================
    
    public static final Integer TRUE_INT = 1;
    
    public static final Integer FALSE_INT = 0;
    
    public static final Integer NOT_DELETED = FALSE_INT;
    
    public static final Integer DELETED = TRUE_INT;
}
