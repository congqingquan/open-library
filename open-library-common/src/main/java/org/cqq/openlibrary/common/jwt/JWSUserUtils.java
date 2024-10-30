package org.cqq.openlibrary.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.annotation.Nullable;
import org.cqq.openlibrary.common.util.HttpContext;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import java.util.Optional;

/**
 * Jws user utils
 *
 * @author Qingquan
 */
@Slf4j
@ConditionalOnBean(JWSAuthConfig.class)
@AutoConfigureAfter(JWSAuthConfig.class)
public class JWSUserUtils {

    private static JWSAuthConfig jwsAuthConfig;


    public JWSUserUtils(JWSAuthConfig jwsAuthConfig) {
        JWSUserUtils.jwsAuthConfig = jwsAuthConfig;
    }
    
    /**
     * ============================== Return nullable value function ==============================
     */
    public static @Nullable Long getNullableUserIdLong() {
        return getNullableUserId(Long.class);
    }
    
    public static @Nullable String getNullableUserIdStr() {
        return getNullableUserId(String.class);
    }
    
    public static <T> @Nullable T getNullableUserId(Class<T> idClass) {
        return getUserId(idClass).orElse(null);
    }
    
    /**
     * ============================== Return optional function ==============================
     */
    
    public static Optional<Long> getUserIdLong() {
        return getUserId(Long.class);
    }
    
    public static Optional<String> getUserIdStr() {
        return getUserId(String.class);
    }
    
    public static <T> Optional<T> getUserId(Class<T> idClass) {
        try {
            String token = HttpContext.getRequest().getHeader(jwsAuthConfig.getAuthHeader());
            
            if (StringUtils.isBlank(token)) {
                log.error("获取的 Token 为空, 认证请求头名称 [{}]", jwsAuthConfig.getAuthHeader());
                return Optional.empty();
            }
            
            Jwt<Header<?>, Claims> jwtContent = JWSUtils.parse(token, jwsAuthConfig.getSecretKey(), 0L);
            T id = jwtContent.getBody().get(jwsAuthConfig.getUserIdPayloadKey(), idClass);
            if (id == null) {
                log.error("获取的用户主键为空. JWT Payload [{}], 用户主键 key [{}]", JSONUtils.toJSONString(jwtContent.getBody()), jwsAuthConfig.getUserIdPayloadKey());
                return Optional.empty();
            }
            
            return Optional.of(id);
        } catch (Exception exception) {
            log.error("获取当前登录用户失败", exception);
            return Optional.empty();
        }
    }
    
    /**
     * ============================== Return user entity function ==============================
     */
    
    public static <T> Optional<T> getUser(Class<T> userClass) {
        try {
            String token = HttpContext.getRequest().getHeader(jwsAuthConfig.getAuthHeader());
            
            if (StringUtils.isBlank(token)) {
                log.error("获取的 Token 为空, 认证请求头 [{}]", jwsAuthConfig.getAuthHeader());
                return Optional.empty();
            }
            
            Jwt<Header<?>, Claims> jwtContent = JWSUtils.parse(token, jwsAuthConfig.getSecretKey(), 0L);
            return Optional.of(
                    JSONUtils.parseObject(
                            JSONUtils.toJSONString(jwtContent.getBody()),
                            userClass
                    )
            );
        } catch (Exception exception) {
            log.error("获取当前登录用户失败", exception);
            return Optional.empty();
        }
    }
}
