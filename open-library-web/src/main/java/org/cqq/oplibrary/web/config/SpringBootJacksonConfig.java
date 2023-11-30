package org.cqq.openlibrary.core.config.jackson;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalDateJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalDateTimeJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalTimeJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.deserializer.Reserve2BigDecimalsJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.serializer.ArrayLongSerializer;
import org.cqq.openlibrary.core.jackson.serializer.ArrayPrimitiveLongSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalDateJacksonSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalDateTimeJacksonSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalTimeJacksonSerializer;
import org.cqq.openlibrary.core.jackson.serializer.Reserve2BigDecimalsJacksonSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * SpringBoot jackson config
 *
 * @author Qingquan.Cong
 */
public class SpringBootJacksonConfig implements Jackson2ObjectMapperBuilderCustomizer {

    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        // 序列化: 应用到 -> 注入的 ObjectMapper.writeValueAsString & SpringMVC 处理接口出参
        // 反序列化: 应用到 -> 注入的 ObjectMapper.readValue

        // 1. Long 转 String 防止前端接收精度丢失
        jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(Long[].class, ArrayLongSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(long[].class, ArrayPrimitiveLongSerializer.instance);

        // 2. 日期序列化
        jacksonObjectMapperBuilder.serializerByType(LocalTime.class, LocalTimeJacksonSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(LocalDate.class, LocalDateJacksonSerializer.instance);
        jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, LocalDateTimeJacksonSerializer.instance);

        jacksonObjectMapperBuilder.deserializerByType(LocalTime.class, LocalTimeJacksonDeserializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, LocalDateJacksonDeserializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class, LocalDateTimeJacksonDeserializer.instance);

        // 3. 浮点数
        jacksonObjectMapperBuilder.serializerByType(BigDecimal.class, Reserve2BigDecimalsJacksonSerializer.instance);
        jacksonObjectMapperBuilder.deserializerByType(BigDecimal.class, Reserve2BigDecimalsJacksonDeserializer.instance);
    }
}