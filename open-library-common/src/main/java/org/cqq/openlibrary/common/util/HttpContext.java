package org.cqq.openlibrary.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.exception.server.IORuntimeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * HTTP request context
 *
 * @author Qingquan
 */
@Slf4j
public class HttpContext {
    
    private HttpContext() {
    }
    
    // ===================================== ServletRequestAttributes =====================================
    
    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }
    
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
    
    // ===================================== Header =====================================
    
    public static List<String> getHeadNames() {
        return EnumerationUtils.toCollection(getRequest().getHeaderNames(), ArrayList::new);
    }
    
    public static <T> Map<String, ? super T> getHeaderMap(Function<String, ? extends T> valueMapping) {
        HttpServletRequest request = getRequest();
        Map<String, T> resultMap = new HashMap<>();
        EnumerationUtils.foreach(
                request.getHeaderNames(),
                headerName ->
                        resultMap.put(
                                headerName,
                                valueMapping.apply(request.getHeader(headerName))
                        )
        );
        return resultMap;
    }
    
    // ===================================== Other =====================================
    
    public static Cookie getCookie(String name) {
        return Arrays
                .stream(getRequest().getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
    
    public static String getRequestDomain() {
        HttpServletRequest request = getRequest();
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.delete(requestURL.length() - request.getRequestURI().length(), requestURL.length()).toString();
    }
    
    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder bodyBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = request.getReader()) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bodyBuilder.append(line);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return bodyBuilder.toString();
    }
}