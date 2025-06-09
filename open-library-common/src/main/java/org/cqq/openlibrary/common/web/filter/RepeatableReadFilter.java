package org.cqq.openlibrary.common.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.cqq.openlibrary.common.web.wrapper.HttpServletRequestWrapperExt;

import java.io.IOException;
import java.util.function.Predicate;

/**
 * Repeatable read filter
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class RepeatableReadFilter implements Filter {
    
    private final Predicate<HttpServletRequest> warpPredicate;
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest httpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (warpPredicate.test(httpServletRequest)) {
            filterChain.doFilter(new HttpServletRequestWrapperExt(httpServletRequest), servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
