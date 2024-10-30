package org.cqq.openlibrary.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.annotation.Nullable;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类(针对于JWT规范下的JWS类别) > 0.9.1 version
 *
 * @author Qingquan
 */
public class JWS091Utils {

    @Getter
    @AllArgsConstructor
    public enum TokenStatus {

        /**
         * 有效：成功解析
         */
        VALID,
        /**
         * 无效：解析失败
         */
        INVALID,
        /**
         * 当前时间已大于Token的Payload中的Expiration字段标识的过期时间戳
         * &&
         * 过期时间量已经超过 parserBuilder().setAllowedClockSkewSeconds(n) 设定的参数 n
         */
        EXPIRED;
    }

    /**
     * Sign
     *
     * @param header               元数据
     * @param payload              载体
     * @param signatureAlgorithm   签名算法
     * @param secretKey            签名密匙（对于不同的签名算法有不同的意义）
     * @param durationMilliseconds 有效时长 (毫秒)
     */
    public static String sign(@Nullable Map<String, Object> header,
                              @Nullable Map<String, Object> payload,
                              SignatureAlgorithm signatureAlgorithm,
                              String secretKey,
                              long durationMilliseconds) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setExpiration(new Date(System.currentTimeMillis() + durationMilliseconds))
                .signWith(new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName()))
                .compact();
    }

    /**
     * Parse token
     *
     * @param token                   需要解析的 token
     * @param signatureAlgorithm      签名算法
     * @param secretKey               签名密匙
     * @param allowedClockSkewSeconds 允许偏差的时间戳
     */
    public static Jwt<Header<?>, Claims> parse(String token,
                                               SignatureAlgorithm signatureAlgorithm,
                                               String secretKey,
                                               long allowedClockSkewSeconds) {
        return Jwts.parserBuilder()
                .setSigningKey(new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName()))
                .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
                .build()
                .parse(token);
    }

    /**
     * Parse token and get token status
     *
     * @param token                   需要解析的 token
     * @param signatureAlgorithm      签名算法
     * @param secretKey               签名密匙
     * @param allowedClockSkewSeconds 允许偏差的时间戳
     */
    public static Tuple2<TokenStatus, Jwt<Header<?>, Claims>> parseAndGetTokenStatus(String token,
                                                                                     SignatureAlgorithm signatureAlgorithm,
                                                                                     String secretKey,
                                                                                     long allowedClockSkewSeconds) {
        Jwt<Header<?>, Claims> parse = null;
        TokenStatus tokenStatus;
        try {
            parse = parse(token, signatureAlgorithm, secretKey, allowedClockSkewSeconds);
            tokenStatus = TokenStatus.VALID;
        } catch (ExpiredJwtException exception) {
            tokenStatus = TokenStatus.EXPIRED;
        } catch (Exception exception) {
            tokenStatus = TokenStatus.INVALID;
        }
        return new Tuple2<>(tokenStatus, parse);
    }

    /**
     * Get token status
     *
     * @param token              需要解析的 token
     * @param signatureAlgorithm 签名算法
     * @param secretKey          签名密匙
     */
    public static TokenStatus getTokenStatus(String token, SignatureAlgorithm signatureAlgorithm, String secretKey) {
        try {
            parse(token, signatureAlgorithm, secretKey, 0L);
        } catch (ExpiredJwtException exception) {
            return TokenStatus.EXPIRED;
        } catch (Exception exception) {
            return TokenStatus.INVALID;
        }
        return TokenStatus.VALID;
    }

    /**
     * Token is valid (Parsable and unexpired)
     *
     * @param token              需要解析的 token
     * @param signatureAlgorithm 签名算法
     * @param secretKey          签名密匙
     */
    public static boolean valid(String token, SignatureAlgorithm signatureAlgorithm, String secretKey) {
        return getTokenStatus(token, signatureAlgorithm, secretKey) == TokenStatus.VALID;
    }

    /**
     * Refresh token (Must call valid function before calling refresh function)
     *
     * @param token                需要刷新的 token
     * @param signatureAlgorithm   签名算法
     * @param secretKey            签名密匙（对于不同的签名算法有不同的意义）
     * @param durationMilliseconds 有效时长 (毫秒)
     */
    public static String refresh(String token, SignatureAlgorithm signatureAlgorithm, String secretKey, long durationMilliseconds) {
        if (!valid(token, signatureAlgorithm, secretKey)) {
            throw new IllegalArgumentException("Expired token");
        }
        Jwt<Header<?>, Claims> parseRes = parse(token, signatureAlgorithm, secretKey, 5L);
        return sign(parseRes.getHeader(), parseRes.getBody(), signatureAlgorithm, secretKey, durationMilliseconds);
    }
}