package org.cqq.openlibrary.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cqq.openlibrary.common.constants.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Common filter
 *
 * @author Qingquan
 */
public class CommonFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. cross-origin
        crossOrigin(request, response);
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }
        // 2. encoding
        utf8Encoding(request, response);
        // 3. do filter
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    private void crossOrigin(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.joining(","))
        );
        response.setHeader(
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                String.join(
                        Constants.COMMA,
                        HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.ORIGIN, HttpHeaders.AUTHORIZATION,
                        HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                        "X-Requested-With"
                )
        );
    }
    
    private void utf8Encoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=UTF-8");
    }
}