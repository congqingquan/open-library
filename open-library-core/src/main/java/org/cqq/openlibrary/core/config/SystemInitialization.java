package org.cqq.openlibrary.core.config;

import org.cqq.openlibrary.core.constants.Constants;

import java.util.TimeZone;

/**
 * System initialization
 *
 * @author Qingquan.Cong
 */
public class SystemInitialization {

    /**
     * 初始化系统配置
     */
    public static void init() {
        initTimeZone(Constants.TIME_ZONE_GMT_8);
        allowRestrictedHeaders();
    }

    /**
     * 初始化时区
     * @param timeZoneId 时区id
     */
    public static void initTimeZone(String timeZoneId) {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneId));
    }

    /**
     * 允许RestTemplate | HttpUrlConnection中设置HttpUrlConnection中禁用发送的请求头
     */
    public static void allowRestrictedHeaders() {
        /**
         *     HttpURLConnection.restrictedHeaders ->
         *
         *     private static final String[] restrictedHeaders = new String[]{
         *          "Access-Control-Request-Headers",
         *          "Access-Control-Request-Method",
         *          "Connection", "Content-Length",
         *          "Content-Transfer-Encoding",
         *          "Host",
         *          "Keep-Alive",
         *          "Origin",
         *          "Trailer",
         *          "Transfer-Encoding",
         *          "Upgrade",
         *          "Via"
         *    }
         */
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }
}