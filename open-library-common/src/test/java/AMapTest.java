import org.cqq.openlibrary.common.sdk.amap.AMapApiSDK;
import org.cqq.openlibrary.common.sdk.amap.request.DistrictRequest;
import org.cqq.openlibrary.common.sdk.amap.response.DistrictResponse;
import org.junit.jupiter.api.Test;

public class AMapTest {
    
    @Test
    public void district() {
        System.setProperty("AMAP_API_KEY", "61b7c10c3785bf25560bcd6ff5f19f4c");
        
        DistrictResponse response = AMapApiSDK.call(DistrictRequest
                .builder()
                .keywords("中国")
                .subDistrict(3)
                .extensions("base")
                .build()
        );
        System.out.println(response);
    }
}
