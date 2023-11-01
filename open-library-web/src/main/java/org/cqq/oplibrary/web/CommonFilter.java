package org.cqq.oplibrary.web;

import org.cqq.openlibrary.core.constants.Constants;
import org.cqq.openlibrary.core.util.CollectionUtils;
import org.cqq.openlibrary.core.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 通用过滤器
 *
 * @author Qingquan.Cong
 */
public class CommonFilter implements Filter {
    
    public static class CrossOriginConfig {
        private final Function<HttpServletRequest, String> allowOriginGetter;
        private final Boolean allowCredentials;
        private final Integer maxAgeMillisecond;
        private String allowMethodsStr;
        private String allowHeadersStr;
        
        public CrossOriginConfig(Function<HttpServletRequest, String> allowOriginGetter,
                                 Boolean allowCredentials, Integer maxAgeMillisecond,
                                 List<String> allowMethods, List<String> allowHeaders) {
            this.allowOriginGetter = allowOriginGetter;
            this.allowCredentials = allowCredentials;
            this.maxAgeMillisecond = maxAgeMillisecond;
            
            if (CollectionUtils.isNotEmpty(allowMethods)) {
                this.allowMethodsStr = String.join(Constants.COMMA, allowMethods);
            }
            if (CollectionUtils.isNotEmpty(allowHeaders)) {
                this.allowHeadersStr = String.join(Constants.COMMA, allowHeaders);
            }
        }
        
        public void setCrossOrigin(HttpServletRequest request, HttpServletResponse response) {
            if (allowOriginGetter != null) {
                response.setHeader("Access-Control-Allow-Origin", allowOriginGetter.apply(request));
            }
            
            if (allowCredentials != null) {
                response.setHeader("Access-Control-Allow-Credentials", allowCredentials.toString());
            }
            
            if (maxAgeMillisecond != null) {
                response.setHeader("Access-Control-Max-Age", maxAgeMillisecond.toString());
            }
            
            if (StringUtils.isNotBlank(allowMethodsStr)) {
                response.setHeader("Access-Control-Allow-Methods", allowMethodsStr);
            }
            if (StringUtils.isNotBlank(allowHeadersStr)) {
                response.setHeader("Access-Control-Allow-Headers", allowHeadersStr);
            }
        }
        
        public static final CrossOriginConfig DEFAULT = new CrossOriginConfig(
                request -> request.getHeader("Origin"),
                true,
                3600,
                Arrays.asList("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE"),
                Arrays.asList("Content-Type", "X-Requested-With", "accept", "Origin",
                        "Authorization", "Access-Control-Request-Method", "Access-Control-Request-Headers")
        );
    }
    
    private final CrossOriginConfig crossOriginConfig;
    
    public CommonFilter() {
        this(CrossOriginConfig.DEFAULT);
    }
    
    public CommonFilter(CrossOriginConfig crossOriginConfig) {
        if (crossOriginConfig == null) {
            throw new IllegalArgumentException("Cross origin config cannot be null");
        }
        this.crossOriginConfig = crossOriginConfig;
    }
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 1. cross-origin
        this.crossOriginConfig.setCrossOrigin(request, response);
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // 2. encoding
        utf8Encoding(request, response);
        // 3. do filter
        filterChain.doFilter(servletRequest, servletResponse);
    }
    
    private void utf8Encoding(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-type", "text/html;charset=UTF-8");
    }
}