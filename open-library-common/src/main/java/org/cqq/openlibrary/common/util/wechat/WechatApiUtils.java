package org.cqq.openlibrary.common.util.wechat;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.cqq.openlibrary.common.component.template.RedisLockTemplate;
import org.cqq.openlibrary.common.exception.biz.WechatException;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.OkHttpUtils;
import org.cqq.openlibrary.common.util.StringUtils;
import org.cqq.openlibrary.common.util.wechat.request.SendSubscribeMessageRequest;
import org.cqq.openlibrary.common.util.wechat.response.GetAccessTokenResponse;
import org.cqq.openlibrary.common.util.wechat.response.JSCode2SessionResponse;
import org.cqq.openlibrary.common.util.wechat.response.WechatResponse;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 微信 API 工具类
 *
 * @author Qingquan
 */
@Slf4j
public class WechatApiUtils {
    
    private WechatApiUtils() {
    }
    
    private static RedissonClient redissonClient;
    
    private static RedisLockTemplate redisLockTemplate;
    
    public static void setRedissonClient(RedissonClient redissonClient) {
        WechatApiUtils.redissonClient = redissonClient;
    }
    
    public static void setRedisLockTemplate(RedisLockTemplate redisLockTemplate) {
        WechatApiUtils.redisLockTemplate = redisLockTemplate;
    }
    
    // ================================================================ Common ================================================================
    
    public static <R extends WechatResponse> R parseResponse(Response response, String apiDesc, Class<R> responseClass, boolean checkWechatResponse) {
        R wechatResponse = OkHttpUtils.consumeResponseBodyString(
                response,
                apiDesc,
                bodyString -> JSONUtils.parseObject(bodyString, responseClass)
        );
        
        if (checkWechatResponse) {
            checkResponse(apiDesc, wechatResponse);
        }
        
        return wechatResponse;
    }
    
    public static void checkResponse(String apiDesc, WechatResponse response) {
        if (response.getErrCode() != 0) {
            log.error("[{}] response error [{}]", apiDesc, response.getErrMsg());
            throw new WechatException(apiDesc + " response error");
        }
    }
    
    // ================================================================ API ================================================================
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
    public static JSCode2SessionResponse jsCode2Session(String appid, String appSecret, String jsCode, boolean checkWechatResponse) {
        Response response = OkHttpUtils.get(
                "https://api.weixin.qq.com/sns/jscode2session",
                Map.of(
                        "appid", appid,
                        "secret", appSecret,
                        "js_code", jsCode,
                        "grant_type", "authorization_code"
                )
        );
        
        return parseResponse(response, "JsCode2Session", JSCode2SessionResponse.class, checkWechatResponse);
    }
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html
    public static GetAccessTokenResponse getAccessToken(String appid, String appSecret, boolean checkWechatResponse) {
        Response response = OkHttpUtils.get(
                "https://api.weixin.qq.com/cgi-bin/token",
                Map.of(
                        "appid", appid,
                        "secret", appSecret,
                        "grant_type", "client_credential"
                )
        );
        
        return parseResponse(response, "Get access token", GetAccessTokenResponse.class, checkWechatResponse);
    }
    
    public static <LX extends Throwable> String getCacheableAccessToken(String appid,
                                                                        String appSecret) throws LX {
        return getCacheableAccessToken(
                appid,
                appSecret,
                "KEY_WECHAT_ACCESS_TOKEN",
                "LOCK_WECHAT_ACCESS_TOKEN",
                3L,
                TimeUnit.SECONDS,
                new WechatException("System busy")
        );
    }
    
    public static <LX extends Throwable> String getCacheableAccessToken(String appid,
                                                                        String appSecret,
                                                                        String accessTokenKey,
                                                                        String lockKey,
                                                                        Long waitTime,
                                                                        TimeUnit timeUnit,
                                                                        LX lockFailedException) throws LX {
        
        if (WechatApiUtils.redissonClient == null) {
            throw new WechatException("Redisson client hasn't been init");
        }
        
        if (WechatApiUtils.redisLockTemplate == null) {
            throw new WechatException("Redisson client hasn't been init");
        }
        
        RBucket<String> accessTokenBucket = WechatApiUtils.redissonClient.getBucket(accessTokenKey);
        String accessToken = accessTokenBucket.get();
        if (StringUtils.isNotBlank(accessToken)) {
            return accessToken;
        }
        
        return WechatApiUtils.redisLockTemplate.execute(
                lockKey,
                waitTime,
                timeUnit,
                lockFailedException,
                () -> {
                    
                    // dcl
                    String getAgainAccessToken = accessTokenBucket.get();
                    if (StringUtils.isNotBlank(getAgainAccessToken)) {
                        return getAgainAccessToken;
                    }
                    
                    // reset token
                    GetAccessTokenResponse accessTokenResponse = WechatApiUtils.getAccessToken(appid, appSecret, true);
                    accessTokenBucket.set(
                            accessTokenResponse.getAccessToken(),
                            Duration.of(Math.max(accessTokenResponse.getExpiresIn() - 3, 1), ChronoUnit.SECONDS)
                    );
                    return accessTokenResponse.getAccessToken();
                }
        );
    }
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
    public static WechatResponse sendSubscribeMessage(SendSubscribeMessageRequest request, boolean checkWechatResponse) {
        Response response = OkHttpUtils
                .postJSON(
                        "https://api.weixin.qq.com/cgi-bin/message/subscribe/send",
                        Map.of(),
                        Map.of("access_token", request.getAccessToken()),
                        request
                );
        
        return parseResponse(response, "Send subscribe message", WechatResponse.class, checkWechatResponse);
    }
}