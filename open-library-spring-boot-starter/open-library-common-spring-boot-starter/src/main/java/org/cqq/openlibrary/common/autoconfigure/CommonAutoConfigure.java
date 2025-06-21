package org.cqq.openlibrary.common.autoconfigure;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.jwt.JWSUserUtils;
import org.cqq.openlibrary.spring.autoconfigure.condition.ConditionalOnProperties;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Common auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(CommonConfig.class)
@Configuration
public class CommonAutoConfigure {
    
    // ======================================== JWS ========================================
    
    @ConditionalOnProperties(properties = {
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.auth-header"),
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.payload.user-id-key"),
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.payload.user-info-key"),
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.signature-algorithm"),
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.secret-key"),
        @ConditionalOnProperties.Property(name = "open-library.common.jws-config.duration"),
    })
    @Component
    public static class JWSInitializer {
        
        @Autowired
        public void init(CommonConfig commonConfig) {
            CommonConfig.JWSConfig jwsConfig = commonConfig.getJwsConfig();
            JWSUserUtils.init(
                    jwsConfig.getAuthHeader(),
                    jwsConfig.getPayload().getUserIdKey(),
                    jwsConfig.getPayload().getUserInfoKey(),
                    jwsConfig.getSecretKey()
            );
        }
    }
    
    // ======================================== Validation ========================================
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.common.validation-config.fail-fast", havingValueRegex = "(?i)(true)|(false)")
    })
    @Bean
    public Validator validator(CommonConfig commonConfig) {
        CommonConfig.ValidationConfig validationConfig = commonConfig.getValidationConfig();
        
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .addProperty("hibernate.validator.fail_fast", String.valueOf(validationConfig.getFailFast()))
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
