package org.cqq.openlibrary.common.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cqq.openlibrary.common.jackson.deserializer.BigDecimalJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalDateJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalDateTimeJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalTimeJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.serializer.BigDecimalJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateTimeJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalTimeJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LongArraySerializer;
import org.cqq.openlibrary.common.jackson.serializer.PrimitiveLongArraySerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * SpringBoot jackson config
 *
 * @author Qingquan
 */
public class SpringBootJacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // 1. Long 转 String 防止前端接收精度丢失
        jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long[].class, LongArraySerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(long[].class, PrimitiveLongArraySerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, BigDecimalJacksonSerializer.instance);

        // 2. 日期序列化
        // 2.1) 序列化: 被应用到 -> 注入的 ObjectMapper.writeValueAsString & SpringMVC 处理接口出参
        jacksonObjectMapperBuilder.serializerByType(LocalTime.class, LocalTimeJacksonSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(LocalDate.class, LocalDateJacksonSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, LocalDateTimeJacksonSerializer.instance);
        // 2.2) 反序列化: 被应用到 -> 注入的 ObjectMapper.readValue
        jacksonObjectMapperBuilder.deserializerByType(LocalTime.class, LocalTimeJacksonDeserializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, LocalDateJacksonDeserializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, LocalDateTimeJacksonDeserializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(BigDecimal.class, BigDecimalJacksonDeserializer.instance);
        
        // 3. 特性配置
        // 3.1) 启用 - 不支持数值匹配枚举成员(by ordinal)
        jacksonObjectMapperBuilder.featuresToEnable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
    }
}