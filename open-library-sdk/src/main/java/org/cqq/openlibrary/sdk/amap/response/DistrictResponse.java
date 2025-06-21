package org.cqq.openlibrary.sdk.amap.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponse extends AMapResponse {

    private Integer count;
    
    private Collection<District> districts;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class District {
        // "" or []
        @JsonProperty ("citycode")
        private Object cityCode;
        @JsonProperty ("adcode")
        private String adCode;
        private String name;
        private String center;
        private String level;
        private Collection<District> districts;
    }
}
