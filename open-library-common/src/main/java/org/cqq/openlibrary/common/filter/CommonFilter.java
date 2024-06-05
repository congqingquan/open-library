package org.cqq.openlibrary.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.cqq.openlibrary.common.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.function.Function;

/**
 * 通用过滤器
 *
 * @author Qingquan.Cong
 */
public class CommonFilter implements Filter {

    // ======================================== CrossOriginConfig ========================================


    @Data
    public static class CrossOriginConfig {

        public static final Collection<String> DEFAULT_ALLOW_METHODS =
                CollectionUtils.newArrayList("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE");

        public static final Collection<String> DEFAULT_ALLOW_HEADERS =
                CollectionUtils.newArrayList(
                        "Content-Type", "X-Requested-With", "accept", "Origin", "Authorization", "Access-Control-Request-Method", "Access-Control-Request" +
                                "-Headers"
                );

        private final Function<HttpServletRequest, String> allowOriginGetter;
        private final Boolean allowCredentials;
        private final Integer maxAgeMillisecond;
        private final Collection<String> allowMethods;
        private final Collection<String> allowHeaders;

        // Cache
        private String allowMethodsStr;
        private String allowHeadersStr;

        public CrossOriginConfig(Function<HttpServletRequest, String> allowOriginGetter,
                                 Boolean allowCredentials, Integer maxAgeMillisecond,
                                 Collection<String> allowMethods, Collection<String> allowHeaders) {
            this.allowOriginGetter = allowOriginGetter;
            this.allowCredentials = allowCredentials;
            this.maxAgeMillisecond = maxAgeMillisecond;
            this.allowMethods = allowMethods;
            this.allowHeaders = allowHeaders;

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
    }

    // ===========================================================================================

    private final CrossOriginConfig crossOriginConfig;

    public CommonFilter() {
        this(
                new CrossOriginConfig(
                        request -> request.getHeader("Origin"),
                        true,
                        3600,
                        CrossOriginConfig.DEFAULT_ALLOW_METHODS,
                        CrossOriginConfig.DEFAULT_ALLOW_HEADERS
                )
        );
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