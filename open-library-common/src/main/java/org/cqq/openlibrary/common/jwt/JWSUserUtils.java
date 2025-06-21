package org.cqq.openlibrary.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.annotation.InitializeStatic;
import org.cqq.openlibrary.common.annotation.Nullable;
import org.cqq.openlibrary.common.util.HttpContext;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.StringUtils;

import java.util.Optional;

/**
 * Jws user utils
 *
 * @author Qingquan
 */
@Slf4j
public class JWSUserUtils {
    
    private static String authHeader;
    
    private static String userIdPayloadKey;
    
    private static String userInfoPayloadKey;
    
    private static String secretKey;
    
    @InitializeStatic
    public static void init(String authHeader,
                            String userIdPayloadKey,
                            String userInfoPayloadKey,
                            String secretKey) {
        JWSUserUtils.authHeader = authHeader;
        JWSUserUtils.userIdPayloadKey = userIdPayloadKey;
        JWSUserUtils.userInfoPayloadKey = userInfoPayloadKey;
        JWSUserUtils.secretKey = secretKey;
    }
    
    // ============================== Get user id function ==============================
    
    public static @Nullable Long getNullableUserIdLong() {
        return getUserIdLong().orElse(null);
    }
    
    public static @Nullable String getNullableUserIdStr() {
        return getUserIdStr().orElse(null);
    }
    
    public static Optional<Long> getUserIdLong() {
        return getPayloadValue(userIdPayloadKey, Long.class);
    }
    
    public static Optional<String> getUserIdStr() {
        return getPayloadValue(userIdPayloadKey, String.class);
    }
    
    public static <T> @Nullable T getNullableUserId(Class<T> idClass) {
        return getPayloadValue(userIdPayloadKey, idClass).orElse(null);
    }
    
    // ============================== Get user info function ==============================
    
    public static <T> @Nullable T getNullableUserInfo(Class<T> userInfoClass) {
        return getUserInfo(userInfoClass).orElse(null);
    }
    
    public static <T> Optional<T> getUserInfo(Class<T> userInfoClass) {
        return getPayloadValue(userInfoPayloadKey, Object.class)
                .map(userInfo ->
                        JSONUtils.parse(
                                JSONUtils.toJSONString(userInfo),
                                userInfoClass
                        )
                );
    }
    
    // ============================== Get payload function ==============================
    
    public static <T> Optional<T> getPayloadValue(String key, Class<T> valueClass) {
        try {
            String token = HttpContext.getRequest().getHeader(authHeader);
            
            if (StringUtils.isBlank(token)) {
                log.warn("获取的 Token 为空, 认证请求头名称 [{}]", authHeader);
                return Optional.empty();
            }
            
            Jwt<Header<?>, Claims> jwtContent = JWSUtils.parse(token, secretKey, 0L);
            T payloadValue = jwtContent.getBody().get(key, valueClass);
            if (payloadValue == null) {
                log.warn("获取 Payload.key [{}] 的值为空. JWT Payload [{}]",
                        key,
                        JSONUtils.toJSONString(jwtContent.getBody())
                );
                return Optional.empty();
            }
            
            return Optional.of(payloadValue);
        } catch (Exception exception) {
            log.error("获取 Payload.key [{}] 的值失败", key, exception);
            return Optional.empty();
        }
    }
}
