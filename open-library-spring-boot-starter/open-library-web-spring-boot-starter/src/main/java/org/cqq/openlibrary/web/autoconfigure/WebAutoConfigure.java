package org.cqq.openlibrary.web.autoconfigure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.enums.DeserializerTypeEnum;
import org.cqq.openlibrary.common.enums.SerializerTypeEnum;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.cqq.openlibrary.common.util.ReflectionUtils;
import org.cqq.openlibrary.spring.autoconfigure.condition.ConditionalOnProperties;
import org.cqq.openlibrary.web.exception.ExceptionHandler;
import org.cqq.openlibrary.web.filter.CrossDomainFilter;
import org.cqq.openlibrary.web.filter.EncodingFilter;
import org.cqq.openlibrary.web.filter.RepeatableReadFilter;
import org.cqq.openlibrary.web.interceptor.MybatisPlusTenantInterceptor;
import org.cqq.openlibrary.web.log.WebLogAdvisor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Web autoconfigure
 *
 * @author Qingquan
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WebConfig.class)
public class WebAutoConfigure {
    
    // ======================================== Log ========================================
    
    @ConditionalOnProperty(name = "open-library.web.log-config.pointcut-expression")
    @Bean
    public Advisor webLogAdvisor(WebConfig webConfig) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(webConfig.getLogConfig().getPointcutExpression());
        return new DefaultPointcutAdvisor(pointcut, new WebLogAdvisor());
    }
    
    // ======================================== Exception ========================================
    
    @ConditionalOnProperty(name = "open-library.web.exception-handler-config.enable", havingValue = "true")
    @Configuration
    public static class ExceptionHandlerConfig {
        @Bean
        public ExceptionHandler exceptionHandler() {
            return new ApiExceptionHandler();
        }
        
        @RestControllerAdvice
        public static class ApiExceptionHandler extends ExceptionHandler {
        }
    }
    
    // ======================================== Jackson ========================================
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.serializers[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.deserializers[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.enable-json-parser-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.enable-json-generator-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.enable-serialization-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.enable-deserialization-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.enable-mapper-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.disable-json-parser-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.disable-json-generator-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.disable-serialization-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.disable-deserialization-features[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.jackson-config.disable-mapper-features[0]"),
    }, allMatch = false)
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(WebConfig webConfig) {
        return builder -> {
            
            WebConfig.JacksonConfig jacksonConfig = webConfig.getJacksonConfig();
            if (jacksonConfig == null) {
                return;
            }
            
            // 1. 序列化: 应用到 -> 注入的 ObjectMapper.writeValueAsString & SpringMVC 处理接口出参
            Collection<SerializerTypeEnum> serializers = jacksonConfig.getSerializers();
            if (CollectionUtils.isNotEmpty(serializers)) {
                serializers.forEach(serializer -> builder.serializerByType(serializer.getType(), serializer.getSerializer()));
            }
            // 2. 反序列化: 应用到 -> 注入的 ObjectMapper.readValue
            Collection<DeserializerTypeEnum> deserializers = jacksonConfig.getDeserializers();
            if (CollectionUtils.isNotEmpty(deserializers)) {
                deserializers.forEach(deserializer -> builder.deserializerByType(deserializer.getType(), deserializer.getDeserializer()));
            }
            
            // 3. 特性配置
            // enable features
            Collection<JsonParser.Feature> enableJsonParserFeatures = jacksonConfig.getEnableJsonParserFeatures();
            if (CollectionUtils.isNotEmpty(enableJsonParserFeatures)) {
                enableJsonParserFeatures.forEach(builder::featuresToEnable);
            }
            Collection<JsonGenerator.Feature> enableJsonGeneratorFeatures = jacksonConfig.getEnableJsonGeneratorFeatures();
            if (CollectionUtils.isNotEmpty(enableJsonGeneratorFeatures)) {
                enableJsonGeneratorFeatures.forEach(builder::featuresToEnable);
            }
            Collection<SerializationFeature> enableSerializationFeatures = jacksonConfig.getEnableSerializationFeatures();
            if (CollectionUtils.isNotEmpty(enableSerializationFeatures)) {
                enableSerializationFeatures.forEach(builder::featuresToEnable);
            }
            Collection<DeserializationFeature> enableDeserializationFeatures = jacksonConfig.getEnableDeserializationFeatures();
            if (CollectionUtils.isNotEmpty(enableDeserializationFeatures)) {
                enableDeserializationFeatures.forEach(builder::featuresToEnable);
            }
            Collection<MapperFeature> enableMapperFeatures = jacksonConfig.getEnableMapperFeatures();
            if (CollectionUtils.isNotEmpty(enableMapperFeatures)) {
                enableMapperFeatures.forEach(builder::featuresToEnable);
            }
            // disable features
            Collection<JsonParser.Feature> disableJsonParserFeatures = jacksonConfig.getDisableJsonParserFeatures();
            if (CollectionUtils.isNotEmpty(disableJsonParserFeatures)) {
                disableJsonParserFeatures.forEach(builder::featuresToDisable);
            }
            Collection<JsonGenerator.Feature> disableJsonGeneratorFeatures = jacksonConfig.getDisableJsonGeneratorFeatures();
            if (CollectionUtils.isNotEmpty(disableJsonGeneratorFeatures)) {
                disableJsonGeneratorFeatures.forEach(builder::featuresToDisable);
            }
            Collection<SerializationFeature> disableSerializationFeatures = jacksonConfig.getDisableSerializationFeatures();
            if (CollectionUtils.isNotEmpty(disableSerializationFeatures)) {
                disableSerializationFeatures.forEach(builder::featuresToDisable);
            }
            Collection<DeserializationFeature> disableDeserializationFeatures = jacksonConfig.getDisableDeserializationFeatures();
            if (CollectionUtils.isNotEmpty(disableDeserializationFeatures)) {
                disableDeserializationFeatures.forEach(builder::featuresToDisable);
            }
            Collection<MapperFeature> disableMapperFeatures = jacksonConfig.getDisableMapperFeatures();
            if (CollectionUtils.isNotEmpty(disableMapperFeatures)) {
                disableMapperFeatures.forEach(builder::featuresToDisable);
            }
        };
    }
    
    // ======================================== Filter ========================================
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.encoding-filter-config.name"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.encoding-filter-config.url-patterns[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.encoding-filter-config.order"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.encoding-filter-config.charset"),
    })
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public FilterRegistrationBean<EncodingFilter> encodingFilter(WebConfig webConfig) {
        WebConfig.FilterConfig.EncodingFilterConfig filterConfig = webConfig.getFilterConfig().getEncodingFilterConfig();
        
        FilterRegistrationBean<EncodingFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setName(filterConfig.getName());
        filterBean.setFilter(
                new EncodingFilter(Charset.forName(filterConfig.getCharset()))
        );
        filterBean.setUrlPatterns(filterConfig.getUrlPatterns());
        filterBean.setOrder(filterConfig.getOrder());
        return filterBean;
    }
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.cross-domain-filter-config.name"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.cross-domain-filter-config.url-patterns[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.cross-domain-filter-config.order"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.cross-domain-filter-config.post-constructor"),
    })
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public FilterRegistrationBean<CrossDomainFilter> crossDomainFilter(WebConfig webConfig) {
        WebConfig.FilterConfig.CrossDomainFilterConfig filterConfig = webConfig.getFilterConfig().getCrossDomainFilterConfig();
        
        FilterRegistrationBean<CrossDomainFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setName(filterConfig.getName());
        filterBean.setFilter(
                new CrossDomainFilter(ReflectionUtils.newInstance(filterConfig.getPostConstructor()))
        );
        filterBean.setUrlPatterns(filterConfig.getUrlPatterns());
        filterBean.setOrder(filterConfig.getOrder());
        return filterBean;
    }
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.repeatable-read-filter-config.name"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.repeatable-read-filter-config.url-patterns[0]"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.repeatable-read-filter-config.order"),
            @ConditionalOnProperties.Property(name = "open-library.web.filter-config.repeatable-read-filter-config.wrap-predicate"),
    })
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public FilterRegistrationBean<RepeatableReadFilter> repeatableReadFilter(WebConfig webConfig) {
        WebConfig.FilterConfig.RepeatableReadFilterConfig filterConfig = webConfig.getFilterConfig().getRepeatableReadFilterConfig();
        
        FilterRegistrationBean<RepeatableReadFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setName(filterConfig.getName());
        filterBean.setFilter(
                new RepeatableReadFilter(ReflectionUtils.newInstance(filterConfig.getWrapPredicate()))
        );
        filterBean.setUrlPatterns(filterConfig.getUrlPatterns());
        filterBean.setOrder(filterConfig.getOrder());
        return filterBean;
    }
    
    // ======================================== Interceptor ========================================
    
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(
                    name = "open-library.web.interceptor-config.mybatis-plus-tenant-interceptor-config.tenant-id-http-header"
            ),
            @ConditionalOnProperties.Property(
                    name = "open-library.web.interceptor-config.mybatis-plus-tenant-interceptor-config.url-patterns"
            )
    })
    @Configuration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @AllArgsConstructor
    public static class WebApplicationConfig implements WebMvcConfigurer {
        
        private final WebConfig webConfig;
        
        @Bean
        public MybatisPlusTenantInterceptor mybatisPlusTenantInterceptor(WebConfig webConfig) {
            return new MybatisPlusTenantInterceptor(
                    webConfig.getInterceptorConfig().getMybatisPlusTenantInterceptorConfig().getTenantIdHttpHeader()
            );
        }
        
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            WebConfig.InterceptorConfig.MybatisPlusTenantInterceptorConfig mybatisPlusTenantInterceptorConfig =
                    webConfig.getInterceptorConfig().getMybatisPlusTenantInterceptorConfig();
            
            registry.addInterceptor(mybatisPlusTenantInterceptor(webConfig))
                    .order(Optional.ofNullable(mybatisPlusTenantInterceptorConfig.getOrder()).orElse(0))
                    .addPathPatterns(new ArrayList<>(mybatisPlusTenantInterceptorConfig.getUrlPatterns()));
        }
    }
}
