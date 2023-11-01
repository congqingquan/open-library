package org.cqq.oplibrary.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     *
     * @return
     */
    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    /**
     * Get servlet req attributes
     *
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * Get cookie
     *
     * @param matchName cookie name
     * @return
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
     *
     * @return
     */
    public static String getRequestDomain() {
        HttpServletRequest request = getRequest();
        StringBuffer requestURL = request.getRequestURL();
        return requestURL.delete(requestURL.length() - request.getRequestURI().length(), requestURL.length()).toString();
    }
}