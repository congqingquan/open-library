package org.cqq.openlibrary.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalDateJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalDateTimeJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.LocalTimeJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.BigDecimalJacksonDeserializer;
import org.cqq.openlibrary.common.jackson.serializer.LongArraySerializer;
import org.cqq.openlibrary.common.jackson.serializer.PrimitiveLongArraySerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateTimeJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalTimeJacksonSerializer;
import org.cqq.openlibrary.common.jackson.serializer.BigDecimalJacksonSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Jackson config
 *
 * @author Qingquan
 */
public class JacksonConfig {

    private final SimpleModule serializationModule = new SimpleModule();

    {
        // 序列化: 应用到 -> 注入的 ObjectMapper.writeValueAsString & SpringMVC 处理接口出参
        // 反序列化: 应用到 -> 注入的 ObjectMapper.readValue

        // 1. Long 转 String 防止前端接收精度丢失
        serializationModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        serializationModule.addSerializer(Long.class, ToStringSerializer.instance);
        serializationModule.addSerializer(Long[].class, LongArraySerializer.instance);
        serializationModule.addSerializer(long[].class, PrimitiveLongArraySerializer.instance);

        // 2. 日期序列化
        serializationModule.addSerializer(LocalTime.class, LocalTimeJacksonSerializer.instance);
        serializationModule.addSerializer(LocalDate.class, LocalDateJacksonSerializer.instance);
        serializationModule.addSerializer(LocalDateTime.class, LocalDateTimeJacksonSerializer.instance);

        serializationModule.addDeserializer(LocalTime.class, LocalTimeJacksonDeserializer.instance);
        serializationModule.addDeserializer(LocalDate.class, LocalDateJacksonDeserializer.instance);
        serializationModule.addDeserializer(LocalDateTime.class, LocalDateTimeJacksonDeserializer.instance);

        // 3. 浮点数
        serializationModule.addSerializer(BigDecimal.class, BigDecimalJacksonSerializer.instance);
        serializationModule.addDeserializer(BigDecimal.class, BigDecimalJacksonDeserializer.instance);
    }

    public ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(serializationModule);
        return objectMapper;
    }
}