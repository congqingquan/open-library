package org.cqq.openlibrary.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.annotation.Nullable;
import org.cqq.openlibrary.common.constants.Constants;

import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类(针对于JWT规范下的JWS类别)
 *
 * @author Qingquan.Cong
 */
@Slf4j
public class JWSV2Utils {

    @Getter
    @AllArgsConstructor
    public enum TokenStatus {

        VALID,

        INVALID,

        // 当前时间已大于Token的Payload中的Expiration字段标识的过期时间戳 && 过期时间量已经超过 parserBuilder().setAllowedClockSkewSeconds(n) 设定的参数 n
        EXPIRED;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParseResult {

        private TokenStatus tokenStatus;

        private Jwt<Header<?>, Claims> jwt;
    }

    /**
     * Sign
     *
     * @param header             元数据
     * @param payload            载体
     * @param signatureAlgorithm 签名算法
     * @param secretKey          签名密匙（对于不同的签名算法有不同的意义）
     * @param durationSeconds    有效时长 (秒)
     * @return
     */
    public static String sign(@Nullable Map<String, Object> header,
                              @Nullable Map<String, Object> payload,
                              SignatureAlgorithm signatureAlgorithm,
                              String secretKey,
                              long durationSeconds) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put("typ", "JWT");
        header.put("alg", signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                // It will reset the value of exp attr in payload after call `setExpiration` method
                .setExpiration(new Date(System.currentTimeMillis() + durationSeconds * 1000))
                // Must pass a byte array of secret key in 0.11.5 version, but need to pass a secret key string in 0.9.1 version.
                // .signWith(signatureAlgorithm, secretKey)
                .signWith(new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName()))
                .compact();
    }

    /**
     * Parse token
     *
     * @param token                   需要解析的 token
     * @param secretKey               签名密匙
     * @param allowedClockSkewSeconds 允许偏差的时间戳
     * @return
     */
    public static Jwt<Header<?>, Claims> parse(String token,
                                               String secretKey,
                                               long allowedClockSkewSeconds) {
        return Jwts.parserBuilder()
                // Must pass a byte array of secret key in 0.11.5 version, but need to pass a secret key string in 0.9.1 version.
                // .signWith(signatureAlgorithm, secretKey)
                .setSigningKey(secretKey.getBytes())
                .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
                .build()
                .parse(token);
    }

    /**
     * Parse token
     *
     * @param token                   需要解析的 token
     * @param secretKey               签名密匙
     * @param allowedClockSkewSeconds 允许偏差的时间戳
     * @return
     */
    public static ParseResult parseToken(String token, String secretKey, long allowedClockSkewSeconds) {
        Jwt<Header<?>, Claims> jtw = null;
        Exception parseException = null;
        TokenStatus tokenStatus = TokenStatus.VALID;
        try {
            jtw = parse(token, secretKey, allowedClockSkewSeconds);
        } catch (ExpiredJwtException exception) {
            parseException = exception;
            tokenStatus = TokenStatus.EXPIRED;
        } catch (Exception exception) {
            parseException = exception;
            tokenStatus = TokenStatus.INVALID;
        } finally {
            if (parseException != null) {
                log.error("Parse token failed. Token status [{}]", tokenStatus.name(), parseException);
            }
        }
        return new ParseResult(tokenStatus, jtw);
    }

    /**
     * Get expiration
     * @param token              需要解析的 token
     * @param secretKey          签名密匙（对于不同的签名算法有不同的意义）
     * @return
     */
    public static LocalDateTime getExpiration(String token, String secretKey) {
        ParseResult parseResult = parseToken(token, secretKey, 0L);
        if (TokenStatus.VALID != parseResult.getTokenStatus()) {
            throw new IllegalArgumentException(String.format("%s TOKEN", parseResult.getTokenStatus().name()));
        }
        Date expiration = parseResult.getJwt().getBody().getExpiration();
        return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Refresh token (Based on current execution time)
     *
     * @param token              需要刷新的 token
     * @param signatureAlgorithm 签名算法
     * @param secretKey          签名密匙（对于不同的签名算法有不同的意义）
     * @param durationSeconds    有效时长 (秒)
     * @return
     */
    public static String refresh(String token, SignatureAlgorithm signatureAlgorithm, String secretKey, long durationSeconds) {
        ParseResult parseResult = parseToken(token, secretKey, 0L);
        if (TokenStatus.VALID != parseResult.getTokenStatus()) {
            throw new IllegalArgumentException(String.format("%s TOKEN", parseResult.getTokenStatus().name()));
        }
        Jwt<Header<?>, Claims> jwt = parseResult.getJwt();
        return sign(jwt.getHeader(), jwt.getBody(), signatureAlgorithm, secretKey, durationSeconds);
    }

    public static void main(String[] args) throws Throwable {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.yyyy_MM_dd_HH_mm_ss);

        String secretKey = "cccccqqqqqcccccqqqqqcccccqqqqqcccccqqqqq";
        SignatureAlgorithm sa = SignatureAlgorithm.HS512;
        Long duration = 3153600000L;
        String token = sign(null, MapBuilder.<String, Object>builder().put("id", 999L).build(), sa, secretKey, duration);

        System.out.println("当前时间: " + dateTimeFormatter.format(LocalDateTime.now()));
        System.out.println("Token 过期时间: " + dateTimeFormatter.format(getExpiration(token, secretKey)));

        String refresh = refresh(token, sa, secretKey, 60L);
        System.out.println("基于当前执行时间第一次刷新: " + dateTimeFormatter.format(getExpiration(refresh, secretKey)));

        Thread.sleep(5000);

        refresh = refresh(refresh, sa, secretKey, 60L);
        System.out.println("基于当前执行时间第二次刷新: " + dateTimeFormatter.format(getExpiration(refresh, secretKey)));
    }
}