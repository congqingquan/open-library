package org.cqq.openlibrary.common.sdk.wechat;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.exception.biz.WechatException;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatGetRequest;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatPostRequest;
import org.cqq.openlibrary.common.sdk.wechat.request.base.WechatRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.base.WechatResponse;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.OkHttpUtils;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信 API SDK
 *
 * @author Qingquan
 */
@Slf4j
public class WechatApiSDK {
    
    private WechatApiSDK() {
    }
    
    public static <R extends WechatResponse<R>> R parseResponse(Response response,
                                                                String apiDesc,
                                                                CheckedFunction<ResponseBody, R, ?> responseBodyMapper,
                                                                boolean checkWechatResponse) {
        R wechatResponse = OkHttpUtils.consumeResponseBody(
                response,
                apiDesc,
                responseBodyMapper
        );
        
        if (checkWechatResponse) {
            checkResponse(apiDesc, wechatResponse);
        }
        
        return wechatResponse;
    }
    
    public static void checkResponse(String apiDesc, WechatResponse<?> response) {
        if (response.getErrCode() != 0) {
            log.error("[{}] response error [{}]", apiDesc, response.getErrMsg());
            throw new WechatException(apiDesc + " response error");
        }
    }
    
    public static <Response extends WechatResponse<Response>, Request extends WechatRequest<Response>> Response execute(Request request) {
        
        log.info("发起微信 API 请求 [{}]", JSONUtils.toJSONString(request));
        
        WechatApiEnum apiEnum = request.apiEnum();
        
        // Access token
        Map<String, String> urlParamMap = new HashMap<>();
        if (apiEnum.getNeedAccessToken()) {
            urlParamMap.put("access_token", request.getAccessToken());
        }
        
        // Request
        okhttp3.Response response;
        if (request instanceof WechatGetRequest<?> getRequest) {
            urlParamMap.putAll(getRequest.buildUrlParamsMap());
            response = OkHttpUtils.get(
                    apiEnum.getUrl(),
                    Map.of(),
                    urlParamMap
            );
            return parseResponse(
                    response,
                    apiEnum.name(),
                    request.responseSupplier().get().parseResponseMapper(),
                    request.isCheckResponse()
            );
        } else if (request instanceof WechatPostRequest<?> _ignore) {
            switch (apiEnum.getMediaType()) {
                case MediaType.APPLICATION_JSON_VALUE -> {
                    response = OkHttpUtils
                            .postJSON(
                                    apiEnum.getUrl(),
                                    Map.of(),
                                    urlParamMap,
                                    request
                            );
                }
                // case ...
                default -> throw new WechatException("暂不支持的 Post media type");
            }
        } else {
            throw new WechatException("暂不支持的 HTTP 请求方式");
        }
        
        // Parse response
        Response res = parseResponse(
                response,
                apiEnum.name(),
                request.responseSupplier().get().parseResponseMapper(),
                request.isCheckResponse()
        );
        log.info("微信 API 响应 [{}]", JSONUtils.toJSONString(res));
        return res;
    }
}