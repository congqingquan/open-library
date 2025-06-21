package org.cqq.openlibrary.sdk.amap.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.sdk.amap.AMapApiEnum;
import org.cqq.openlibrary.sdk.amap.response.DistrictResponse;

/**
 * 行政区域查询
 *
 * @author Qingquan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DistrictRequest extends AMapRequest<DistrictResponse> {
    
    private String keywords;
    
    @JsonProperty("subdistrict")
    private Integer subDistrict;
    
    private String extensions;
    
    @Override
    public AMapApiEnum apiEnum() {
        return AMapApiEnum.DISTRICT;
    }
    
    @Override
    public Class<DistrictResponse> responseClass() {
        return DistrictResponse.class;
    }
}
