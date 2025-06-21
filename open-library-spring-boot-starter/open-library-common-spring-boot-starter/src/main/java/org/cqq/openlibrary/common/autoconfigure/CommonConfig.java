package org.cqq.openlibrary.common.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Common config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.common")
public class CommonConfig {
    
    private JWSConfig jwsConfig;
    
    private ValidationConfig validationConfig;
    
    @Data
    public static class JWSConfig {
        private String authHeader;
        
        private Payload payload;
        
        private String signatureAlgorithm;
        
        private String secretKey;
        
        private Long duration;
        
        @Data
        public static class Payload {
            private String userIdKey;
            private String userInfoKey;
        }
    }
    
    @Data
    public static class ValidationConfig {
        private Boolean failFast;
    }
}
