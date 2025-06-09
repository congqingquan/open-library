package org.cqq.openlibrary.common.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Net utils
 *
 * @author Qingquan
 */
@Slf4j
public class NetUtils {
    
    private NetUtils() {
    }
    
    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * URL编码
     * @param str url string
     * @param convertPlus URL编码后默认空格被转为+号，是否需要再次转换为%20
     */
    public static String URLEncode(String str, boolean convertPlus) {
        String res = "";
        res = URLEncoder.encode(str, StandardCharsets.UTF_8);
        if (convertPlus) {
            res = res.replace("+", "%20");
        }
        return res;
    }
    
    /**
     * 转为 GET 请求参数 URI
     */
    public static String toGetRequestParamsUri(Map<String, ?> params) {
        if (MapUtils.isEmpty(params)) {
            return "";
        }
        String paramsStr = params
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        return "?" + paramsStr;
    }
    
    /**
     * 解析 GET 请求参数
     */
    public static Map<String, String> parseGetRequestParams(String uri) {
        if (StringUtils.isBlank(uri)) {
            return new HashMap<>();
        }
        // 以第一个 '?' 为准，后面的所有内容皆为 GET 请求参数
        int i = uri.indexOf("?");
        if (i == -1) {
            return new HashMap<>();
        }
        
        // 截取有效的参数部分
        uri = uri.substring(uri.indexOf('?') + 1);
        String[] pairs = uri.split("&");
        Map<String, String> paramMap = MapUtils.newHashMap(pairs.length);
        for (String pair : pairs) {
            String[] pairArr = pair.split("=");
            // case: key=value
            if (pairArr.length == 2) {
                paramMap.put(pairArr[0], pairArr[1]);
            }
            // case: key=
            else if (pairArr.length == 1 && StringUtils.isNotBlank(pairArr[0])) {
                paramMap.put(pairArr[0], "");
            } else {
                log.error("未能解析的 GET 请求参数对 [{}]", pair);
            }
        }
        return paramMap;
    }
}