package org.cqq.openlibrary.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.cqq.openlibrary.web.wrapper.HttpServletRequestWrapperExt;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * Repeatable read filter
 *
 * @author Qingquan
 */
@AllArgsConstructor
public class RepeatableReadFilter implements Filter {
    
    private final WrapPredicate warpPredicate;
    
    @FunctionalInterface
    public interface WrapPredicate {
        boolean test(HttpServletRequest httpServletRequest);
    }
    
    public static class JSONRepeatableReadFilterWrapPredicate implements RepeatableReadFilter.WrapPredicate {
        @Override
        public boolean test(HttpServletRequest httpServletRequest) {
            return MediaType.APPLICATION_JSON.toString().equals(httpServletRequest.getContentType());
        }
    }
    
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
