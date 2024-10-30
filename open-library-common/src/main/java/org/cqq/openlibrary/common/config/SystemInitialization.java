package org.cqq.openlibrary.common.config;

import org.cqq.openlibrary.common.constants.Constants;

import java.util.TimeZone;

/**
 * System initialization
 *
 * @author Qingquan
 */
public class SystemInitialization {

    public static void init() {
        initTimeZone(Constants.TIME_ZONE_GMT_8);
        allowRestrictedHeaders();
    }

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