package org.cqq.openlibrary.common.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * JWS 鉴权配置
 *
 * @author Qingquan
 */
@Data
public class JWSAuthConfig {

    @Value("${auth.header:Authorization}")
    private String authHeader;
    
    @Value("${auth.jws.payload.user-id-key:id}")
    private String userIdPayloadKey;
    
    @Value("${auth.jws.payload.user-info-key:id}")
    private String userInfoPayloadKey;
    
    @Value("${auth.jws.signature-algorithm:HS256}")
    private String signatureAlgorithm;
    
    @Value("${auth.jws.secret-key:None}")
    private String secretKey;
    
    @Value("${auth.jws.duration:604800000}")
    private Long duration;
}