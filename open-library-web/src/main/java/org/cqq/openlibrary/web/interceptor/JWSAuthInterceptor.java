package org.cqq.openlibrary.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cqq.openlibrary.common.exception.client.UnauthenticatedException;
import org.cqq.openlibrary.common.jwt.JWSUtils;
import org.cqq.openlibrary.common.util.StringUtils;

/**
 * JWS auth interceptor
 *
 * @author Qingquan
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JWSAuthInterceptor extends AuthInterceptor {
    
    private final String authHeader;
    
    private final String secretKey;
    
    @Override
    public void checkAuthenticated(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(authHeader);
        if (StringUtils.isBlank(token)) {
            throw new UnauthenticatedException("No login");
        }
        JWSUtils.ParseResult parseResult = JWSUtils.parseToken(token, secretKey, 0L);
        JWSUtils.TokenStatus tokenStatus = parseResult.getTokenStatus();
        if (JWSUtils.TokenStatus.EXPIRED == tokenStatus) {
            throw new UnauthenticatedException("Expired token");
        } else if (JWSUtils.TokenStatus.INVALID == tokenStatus) {
            throw new UnauthenticatedException("Invalid Token");
        }
    }
}