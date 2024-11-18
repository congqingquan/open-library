import org.cqq.openlibrary.common.util.wechat.WechatApiUtils;
import org.cqq.openlibrary.common.util.wechat.response.GetAccessTokenResponse;
import org.cqq.openlibrary.common.util.wechat.response.JSCode2SessionResponse;
import org.junit.jupiter.api.Test;

/**
 * @author Qingquan
 */
public class WechatApiUtilsTest {
    
    private final String appid = "";
    private final String appSecret = "";
    
    @Test
    public void  getAccessTokenTest() {
        GetAccessTokenResponse response = WechatApiUtils.getAccessToken(appid, appSecret, true);
        System.out.println(response);
    }
    
    @Test
    public void jsCode2SessionTest() {
        JSCode2SessionResponse jsCode2SessionResponse = WechatApiUtils.jsCode2Session(
                appid,
                appSecret,
                "adasdasdasdasddas",
                true
        );
        System.out.println(jsCode2SessionResponse);
    }
}
