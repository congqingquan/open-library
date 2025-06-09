package org.cqq.openlibrary.common.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.cqq.openlibrary.common.constants.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Default filter
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class DefaultFilter implements Filter {
    
    private final BiConsumer<HttpServletRequest, HttpServletResponse> postSetting;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        
        if (!(servletRequest instanceof HttpServletRequest httpServletRequest) || !(servletResponse instanceof HttpServletResponse httpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        // 1. cross-origin
        crossOrigin(httpServletRequest, httpServletResponse);
        // 2. encoding
        utf8Encoding(httpServletRequest, httpServletResponse);
        // 3. post setting
        postSetting.accept(httpServletRequest, httpServletResponse);
        if (HttpMethod.OPTIONS.matches(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return;
        }
        // 3. do filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
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
                        HttpHeaders.CONTENT_TYPE,
                        HttpHeaders.ACCEPT,
                        HttpHeaders.ORIGIN,
                        HttpHeaders.AUTHORIZATION,
                        HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                        HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                        "X-Requested-With"
                )
        );
    }
    
    private void utf8Encoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}