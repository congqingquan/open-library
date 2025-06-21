package org.cqq.openlibrary.web.autoconfigure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.cqq.openlibrary.web.jackson.deserializer.StringToLocalDateDeserializer;
import org.cqq.openlibrary.web.jackson.deserializer.StringToLocalDateTimeDeserializer;
import org.cqq.openlibrary.web.jackson.deserializer.StringToLocalTimeDeserializer;
import org.cqq.openlibrary.web.jackson.serializer.BoxingLongArrayToStringArraySerializer;
import org.cqq.openlibrary.web.jackson.serializer.LocalDateTimeToStringSerializer;
import org.cqq.openlibrary.web.jackson.serializer.LocalDateToStringSerializer;
import org.cqq.openlibrary.web.jackson.serializer.LocalTimeToStringSerializer;
import org.cqq.openlibrary.web.jackson.serializer.PrimitiveLongArrayToStringArraySerializer;
import org.cqq.openlibrary.web.jackson.serializer.Scale2BigDecimalToStringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        
        @Getter
        @AllArgsConstructor
        public enum SerializerTypeEnum {
            
            // Long
            PRIMITIVE_LONG_TO_STRING(Long.TYPE, ToStringSerializer.instance),
            BOXING_LONG_TO_STRING(Long.class, ToStringSerializer.instance),
            PRIMITIVE_LONG_ARRAY_TO_STRING_ARRAY(long[].class, PrimitiveLongArrayToStringArraySerializer.instance),
            BOXING_LONG_ARRAY_TO_STRING_ARRAY(Long[].class, BoxingLongArrayToStringArraySerializer.instance),
            
            // Datetime
            LOCAL_TIME_TO_STRING(LocalTime.class, LocalTimeToStringSerializer.instance),
            LOCAL_DATE_TO_STRING(LocalDate.class, LocalDateToStringSerializer.instance),
            LOCAL_DATE_TIME_TO_STRING(LocalDateTime.class, LocalDateTimeToStringSerializer.instance),
            
            // BigDecimal
            SCALE2_BIG_DECIMAL_TO_STRING(BigDecimal.class, Scale2BigDecimalToStringSerializer.instance),
            ;
            
            private final Class<?> type;
            
            private final JsonSerializer<?> serializer;
        }
        
        @Getter
        @AllArgsConstructor
        public enum DeserializerTypeEnum {
            // Datetime
            STRING_TO_LOCAL_TIME(LocalTime.class, StringToLocalTimeDeserializer.instance),
            STRING_TO_LOCAL_DATE(LocalDate.class, StringToLocalDateDeserializer.instance),
            STRING_TO_LOCAL_DATE_TIME(LocalDateTime.class, StringToLocalDateTimeDeserializer.instance),
            ;
            
            private final Class<?> type;
            
            private final JsonDeserializer<?> deserializer;
        }
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
