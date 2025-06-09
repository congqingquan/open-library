package org.cqq.openlibrary.common.sdk.amap.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * AMap response
 *
 * @author Qingquan
 */
@Data
public class AMapResponse {
    
    /**
     * 1：成功；0：失败
     */
    private Integer status;
    
    /**
     * <a href="https://lbs.amap.com/api/webservice/guide/tools/info/">info & infocode mapping</a>
     */
    private String info;
    
    @JsonProperty("infocode")
    private Long infoCode;
}
