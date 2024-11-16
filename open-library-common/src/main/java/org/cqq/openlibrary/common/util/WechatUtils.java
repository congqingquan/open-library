package org.cqq.openlibrary.common.util;

import okhttp3.Response;
import okhttp3.ResponseBody;
import org.cqq.openlibrary.common.domain.wechat.JSCode2SessionVO;
import org.cqq.openlibrary.common.exception.WechatException;

import java.util.Map;

/**
 * 微信工具类
 *
 * @author Qingquan
 */
public class WechatUtils {
    
    private WechatUtils() {
    }
    
    // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
    public static JSCode2SessionVO jsCode2Session(String appid, String appSecret, String jsCode) {
        
        try (Response response = OkHttpUtils.get(
                "https://api.weixin.qq.com/sns/jscode2session",
                Map.of(
                        "appid", appid,
                        "secret", appSecret,
                        "js_code", jsCode,
                        "grant_type", "authorization_code"
                )
        )) {
            ResponseBody body = response.body();
            if (body == null) {
                throw new WechatException("JsCode2Session API 响应的 ResponseBody 为空");
            }
            
            String bodyString = body.string();
            if (!response.isSuccessful()) {
                throw new WechatException("JsCode2Session API 响应失败, " + bodyString);
            }
            
            return JSONUtils.parseObject(bodyString, JSCode2SessionVO.class);
        } catch (Throwable throwable) {
            throw new WechatException("JsCode2Session API 调用失败", throwable);
        }
    }
}
