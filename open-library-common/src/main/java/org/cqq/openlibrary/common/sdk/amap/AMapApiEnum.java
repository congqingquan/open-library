package org.cqq.openlibrary.common.sdk.amap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

/**
 * AMap api enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum AMapApiEnum {
    
    // https://lbs.amap.com/api/webservice/guide/api/district
    DISTRICT("https://restapi.amap.com/v3/config/district", HttpMethod.GET, null, true),
    ;
    
    private final String url;
    
    private final HttpMethod method;
    
    private final String mediaType;
    
    private final Boolean needApiKey;
}
