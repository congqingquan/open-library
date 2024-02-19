package org.cqq.oplibrary.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * HTTP request context
 *
 * @author CongQingquan
 */
public class HttpContext {
    
    /**
     * Get req instance
     */
    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }
    
    /**
     * Get response instance
     */
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }
    
    /**
     * Get servlet req attributes
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    
    /**
     * Get header names
     */
    public static List<String> getHeadNames() {
        Enumeration<String> headerNames = getRequest().getHeaderNames();
        List<String> resultList = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            resultList.add(headerNames.nextElement());
        }
        return resultList;
    }
    
    /**
     * Get value of all header
     */
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
    
    
    /**
     * Get multiple value of header
     */
    public static <T> List<T> getHeaders(String name, Function<String, T> mapping) {
        Enumeration<String> headers = getRequest().getHeaders(name);
        List<T> values = new ArrayList<>();
        while (headers.hasMoreElements()) {
            values.add(mapping.apply(headers.nextElement()));
        }
        return values;
    }
    
    /**
     * Get multiple value of all header
     */
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
    
    /**
     * Get cookie
     */
    public static Cookie getCookie(String matchName) {
        Cookie[] cookies = getRequest().getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (matchName.equals(cookies[i].getName())) {
                return cookies[i];
            }
        }
        return null;
    }
    
    /**
     * Get req domain
     */
    public static String getRequestDomain() {
        HttpServletRequest request = getRequest();
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.delete(requestURL.length() - request.getRequestURI().length(), requestURL.length()).toString();
    }
}