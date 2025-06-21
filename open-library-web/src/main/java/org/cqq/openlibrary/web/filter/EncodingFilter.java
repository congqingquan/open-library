package org.cqq.openlibrary.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Encoding filter
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class EncodingFilter implements Filter {
    
    private final Charset charset;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest httpServletRequest) || !(servletResponse instanceof HttpServletResponse httpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 1. encoding
        httpServletRequest.setCharacterEncoding(charset.name());
        httpServletResponse.setCharacterEncoding(charset.name());
        // 2. do filter
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}