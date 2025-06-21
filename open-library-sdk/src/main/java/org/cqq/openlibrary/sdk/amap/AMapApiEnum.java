package org.cqq.openlibrary.sdk.amap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.enums.HttpMethodEnum;

/**
 * AMap api enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum AMapApiEnum {
    
    // https://lbs.amap.com/api/webservice/guide/api/district
    DISTRICT("https://restapi.amap.com/v3/config/district", HttpMethodEnum.GET, null, true),
    ;
    
    private final String url;
    
    private final HttpMethodEnum method;
    
    private final String mediaType;
    
    private final Boolean needApiKey;
}
