package org.cqq.openlibrary.sdk.amap.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.cqq.openlibrary.sdk.amap.AMapApiEnum;
import org.cqq.openlibrary.sdk.amap.response.AMapResponse;

/**
 * AMap request
 *
 * @author Qingquan
 */
@Data
public abstract class AMapRequest<Response extends AMapResponse> {
    
    @JsonProperty("key")
    private String apiKey;
    
    public abstract AMapApiEnum apiEnum();
    
    public abstract Class<? extends Response> responseClass();
}
