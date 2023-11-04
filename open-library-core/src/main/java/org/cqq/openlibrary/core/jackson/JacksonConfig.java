package org.cqq.openlibrary.core.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalDateJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalDateTimeJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.deserializer.LocalTimeJacksonDeserializer;
import org.cqq.openlibrary.core.jackson.serializer.ArrayLongSerializer;
import org.cqq.openlibrary.core.jackson.serializer.ArrayPrimitiveLongSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalDateJacksonSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalDateTimeJacksonSerializer;
import org.cqq.openlibrary.core.jackson.serializer.LocalTimeJacksonSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JacksonConfig {

    private final SimpleModule serializationModule = new SimpleModule();

    {
        // 1. Long 转 String 防止前端接收精度丢失
        serializationModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        serializationModule.addSerializer(Long.class, ToStringSerializer.instance);
        serializationModule.addSerializer(Long[].class, ArrayLongSerializer.instance);
        serializationModule.addSerializer(long[].class, ArrayPrimitiveLongSerializer.instance);

        // 2. 日期序列化
        // 2.1) 序列化: 被应用到 -> 注入的 ObjectMapper.writeValueAsString & SpringMVC 处理接口出参
        serializationModule.addSerializer(LocalTime.class, LocalTimeJacksonSerializer.instance);
        serializationModule.addSerializer(LocalDate.class, LocalDateJacksonSerializer.instance);
        serializationModule.addSerializer(LocalDateTime.class, LocalDateTimeJacksonSerializer.instance);

        // 2.2) 反序列化: 被应用到 -> 注入的 ObjectMapper.readValue
        serializationModule.addDeserializer(LocalTime.class, LocalTimeJacksonDeserializer.instance);
        serializationModule.addDeserializer(LocalDate.class, LocalDateJacksonDeserializer.instance);
        serializationModule.addDeserializer(LocalDateTime.class, LocalDateTimeJacksonDeserializer.instance);
    }

    public ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(serializationModule);
        return objectMapper;
    }
}