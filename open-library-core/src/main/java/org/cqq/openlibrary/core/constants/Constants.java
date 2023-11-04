package org.cqq.openlibrary.core.constants;

import java.util.TimeZone;

/**
 * Constants
 *
 * @author Qingquan.Cong
 */
public class Constants {

    /**
     * ================================== 时间 ==================================
     */

    /**
     * 时间格式
     */
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

    /**
     * 逗号
     */
    public static final String COMMA = ",";

    /**
     * GMT 时区: 格林威治标准时间的英文缩写(Greenwich Mean Time 格林尼治标准时间),是世界标准时间.
     *
     * GMT+8 是格林威治时间+8小时.中国所在时区就是GMT+8.
     */
    public static final String TIME_ZONE_GMT_8 = "GMT+8";

    /**
     * 默认时区
     */
    public static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE_GMT_8);
    
    /**
     * 未删除
     */
    public static final int NO_DELETED = 0;
    
    /**
     * 已删除
     */
    public static final int DELETED = 1;
}