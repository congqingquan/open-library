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
        // 1. 默认字符集
        // 修改默认字符集，只能在 JVM 启动时指定 JVM 启动参数：-Dfile.encoding=UTF-8 来修改 Charset.defaultCharset() 的返回结果
        // 程序运行时执行：System.setProperty("file.encoding", StandardCharsets.UTF_8.name()); 是无法生效的
        
        // 2. 默认时间区
        TimeZone.setDefault(TimeZone.getTimeZone(Constants.TIME_ZONE_GMT_8));
        
        // 3. 解除 HttpURLConnection / Spring RestTemplate 默认的请求头限制
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