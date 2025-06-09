package org.cqq.openlibrary.common.sdk.amap;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.exception.server.IORuntimeException;
import org.cqq.openlibrary.common.sdk.amap.request.AMapRequest;
import org.cqq.openlibrary.common.sdk.amap.response.AMapResponse;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.cqq.openlibrary.common.util.OkHttpUtils;
import org.cqq.openlibrary.common.util.StringUtils;
import org.springframework.http.HttpMethod;

import java.io.IOException;

/**
 * AMap api sdk
 *
 * @author Qingquan
 */
@Slf4j
public class AMapApiSDK {
    
    private static final Integer API_SUCCESS = 1;
    
    private AMapApiSDK() {
    }
    
    private static <Request extends AMapRequest<?>> void setApiKey(Request request) {
        String apiKey = System.getenv("AMAP_API_KEY");
        if (StringUtils.isBlank(apiKey)) {
            throw new AMapException("Cannot get API_KEY in env");
        }
        request.setApiKey(apiKey);
    }
    
    public static <Request extends AMapRequest<Response>, Response extends AMapResponse> Response call(Request request) {
        
        AMapApiEnum aMapApiEnum = request.apiEnum();
        HttpMethod httpMethod = aMapApiEnum.getMethod();
        
        setApiKey(request);
        
        okhttp3.Response response;
        if (httpMethod.equals(HttpMethod.GET)) {
            response = OkHttpUtils.get(request.apiEnum().getUrl(), JSONUtils.parse2Map(JSONUtils.toJSONString(request)));
        } else {
            throw new IllegalArgumentException("HttpMethod not support");
        }
        
        int code = response.code();
        if (code != 200) {
            log.error("调用高德接口异常 [{}-{}]", code, response.message());
            throw new AMapException("调用高德异常");
        }
        ResponseBody body = response.body();
        if (body == null) {
            throw new AMapException("高德响应内容为空");
        }
        try {
            Response responseBody = JSONUtils.parse(body.string(), request.responseClass());
            if (!API_SUCCESS.equals(responseBody.getStatus())) {
                log.error("高德响应状态异常 [{}]", JSONUtils.toJSONString(responseBody));
                throw new AMapException("高德响应状态异常");
            }
            return responseBody;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}