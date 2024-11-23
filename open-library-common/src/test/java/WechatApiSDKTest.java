import org.cqq.openlibrary.common.sdk.wechat.WechatApiSDK;
import org.cqq.openlibrary.common.sdk.wechat.request.GetAccessTokenRequest;
import org.cqq.openlibrary.common.sdk.wechat.request.JSCode2SessionRequest;
import org.cqq.openlibrary.common.sdk.wechat.response.GetAccessTokenResponse;
import org.cqq.openlibrary.common.sdk.wechat.response.JSCode2SessionResponse;
import org.junit.jupiter.api.Test;

/**
 * @author Qingquan
 */
public class WechatApiSDKTest {
    
    private final String appid = "";
    private final String appSecret = "";
    
    @Test
    public void  getAccessTokenTest() {
        GetAccessTokenRequest request = new GetAccessTokenRequest();
        request.setAppid(appid);
        request.setAppSecret(appSecret);
        
        GetAccessTokenResponse response = WechatApiSDK.execute(request);
        System.out.println(response);
    }
    
    @Test
    public void jsCode2SessionTest() {
        JSCode2SessionRequest request = new JSCode2SessionRequest();
        request.setJsCode("adasdasdasdasddas");
        request.setAppid(appid);
        request.setAppSecret(appSecret);
        
        JSCode2SessionResponse response = WechatApiSDK.execute(request);
        System.out.println(response);
    }
}
