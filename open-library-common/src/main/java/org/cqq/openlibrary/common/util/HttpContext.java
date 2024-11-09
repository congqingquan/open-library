package org.cqq.openlibrary.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * HTTP request context
 *
 * @author Qingquan
 */
public class HttpContext {
    
    private HttpContext() {
    }
    
    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }
    
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    
    public static List<String> getHeadNames() {
        Enumeration<String> headerNames = getRequest().getHeaderNames();
        List<String> resultList = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            resultList.add(headerNames.nextElement());
        }
        return resultList;
    }
    
    public static Map<String, String> getAllHeader() {
        Map<String, String> resultMap = new HashMap<>();
        HttpServletRequest request = getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            resultMap.put(headerName, request.getHeader(headerName));
        }
        return resultMap;
    }
    
    public static <T> List<T> getHeaders(String name, Function<String, T> mapping) {
        Enumeration<String> headers = getRequest().getHeaders(name);
        List<T> values = new ArrayList<>();
        while (headers.hasMoreElements()) {
            values.add(mapping.apply(headers.nextElement()));
        }
        return values;
    }

    public static Map<String, List<String>> getAllHeaders() {
        Map<String, List<String>> resultMap = new HashMap<>();
        HttpServletRequest request = HttpContext.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            List<String> values = getHeaders(headerName, Function.identity());
            resultMap.put(headerName, values);
        }
        return resultMap;
    }
    
    public static Cookie getCookie(String matchName) {
        Cookie[] cookies = getRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (matchName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
    
    public static String getRequestDomain() {
        HttpServletRequest request = getRequest();
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.delete(requestURL.length() - request.getRequestURI().length(), requestURL.length()).toString();
    }
}