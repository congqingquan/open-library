package org.cqq.openlibrary.web.autoconfigure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;
import org.cqq.openlibrary.common.enums.DeserializerTypeEnum;
import org.cqq.openlibrary.common.enums.SerializerTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collection;

/**
 * Web config
 *
 * @author Qingquan
 */
@Data
@ConfigurationProperties(prefix = "open-library.web")
public class WebConfig {

    private LogConfig logConfig;
    
    private ExceptionHandlerConfig exceptionHandlerConfig;
    
    private JacksonConfig jacksonConfig;
    
    private FilterConfig filterConfig;
    
    // ========================================== Log ==========================================
    
    @Data
    public static class LogConfig {
        private String pointcutExpression;
    }
    
    // ========================================== ExceptionHandler ==========================================
    
    @Data
    public static class ExceptionHandlerConfig {
        private Boolean enable;
    }
    
    // ========================================== Jackson ==========================================
    
    @Data
    public static class JacksonConfig {
        
        private Collection<SerializerTypeEnum> serializers;
        
        private Collection<DeserializerTypeEnum> deserializers;
        
        /**
         * @see com.fasterxml.jackson.core.JsonParser.Feature
         * @see com.fasterxml.jackson.core.JsonGenerator.Feature
         * @see com.fasterxml.jackson.databind.SerializationFeature
         * @see com.fasterxml.jackson.databind.DeserializationFeature
         * @see com.fasterxml.jackson.databind.MapperFeature
         */
        private Collection<JsonParser.Feature> enableJsonParserFeatures;
        private Collection<JsonGenerator.Feature> enableJsonGeneratorFeatures;
        private Collection<SerializationFeature> enableSerializationFeatures;
        private Collection<DeserializationFeature> enableDeserializationFeatures;
        private Collection<MapperFeature> enableMapperFeatures;
        
        /**
         * @see com.fasterxml.jackson.core.JsonParser.Feature
         * @see com.fasterxml.jackson.core.JsonGenerator.Feature
         * @see com.fasterxml.jackson.databind.SerializationFeature
         * @see com.fasterxml.jackson.databind.DeserializationFeature
         * @see com.fasterxml.jackson.databind.MapperFeature
         */
        private Collection<JsonParser.Feature>     disableJsonParserFeatures;
        private Collection<JsonGenerator.Feature>  disableJsonGeneratorFeatures;
        private Collection<SerializationFeature>   disableSerializationFeatures;
        private Collection<DeserializationFeature> disableDeserializationFeatures;
        private Collection<MapperFeature>          disableMapperFeatures;
    }
    
    // ========================================== Filter ==========================================
    
    @Data
    public static class FilterConfig {
        private EncodingFilterConfig encodingFilterConfig;
        private CrossDomainFilterConfig crossDomainFilterConfig;
        private RepeatableReadFilterConfig repeatableReadFilterConfig;
        
        @Data
        public static class EncodingFilterConfig {
            private String name;
            private Collection<String> urlPatterns;
            private Integer order;
            private String charset;
        }
        
        @Data
        public static class CrossDomainFilterConfig {
            private String name;
            private Collection<String> urlPatterns;
            private Integer order;
            private Class<? extends org.cqq.openlibrary.web.filter.CrossDomainFilter.PostConstructor> postConstructor;
        }
        
        @Data
        public static class RepeatableReadFilterConfig {
            private String name;
            private Collection<String> urlPatterns;
            private Integer order;
            private Class<? extends org.cqq.openlibrary.web.filter.RepeatableReadFilter.WrapPredicate> wrapPredicate;
        }
    }
}
