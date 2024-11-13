package org.cqq.openlibrary.common.config;

import org.cqq.openlibrary.common.constants.Constants;

import java.util.TimeZone;

/**
 * System initializer
 *
 * @author Qingquan
 */
public class SystemInitializer {

    public static void init() {
        
        TimeZone.setDefault(TimeZone.getTimeZone(Constants.TIME_ZONE_GMT_8));
        
        /*
              HttpURLConnection.restrictedHeaders ->
         
              private static final String[] restrictedHeaders = new String[]{
                   "Access-Control-Request-Headers",
                   "Access-Control-Request-Method",
                   "Connection", "Content-Length",
                   "Content-Transfer-Encoding",
                   "Host",
                   "Keep-Alive",
                   "Origin",
                   "Trailer",
                   "Transfer-Encoding",
                   "Upgrade",
                   "Via"
             }
         */
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
    }
}