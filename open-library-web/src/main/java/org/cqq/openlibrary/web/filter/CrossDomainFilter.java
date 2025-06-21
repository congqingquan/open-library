package org.cqq.openlibrary.web.filter;

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
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Cross domain filter
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class CrossDomainFilter implements Filter {
    
    private final PostConstructor postConstructor;
    
    @FunctionalInterface
    public interface PostConstructor {
        void construct(HttpServletRequest request, HttpServletResponse response);
    }
    
    public static class TenantIdCrossDomainFilterPostConstructor implements CrossDomainFilter.PostConstructor {
        @Override
        public void construct(HttpServletRequest request, HttpServletResponse response) {
            String allowHeaders = response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
            String finalAllowHeaders = String.join(Constants.COMMA, allowHeaders, "TenantId");
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, finalAllowHeaders);
        }
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        
        if (!(servletRequest instanceof HttpServletRequest httpServletRequest) || !(servletResponse instanceof HttpServletResponse httpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        
        // 1. cross-origin
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        httpServletResponse.setHeader(
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.joining(","))
        );
        httpServletResponse.setHeader(
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
        // 2. post construct
        postConstructor.construct(httpServletRequest, httpServletResponse);
        if (HttpMethod.OPTIONS.matches(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return;
        }
        // 3. do filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}