package org.cqq.openlibrary.common.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalDateDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalDateTimeDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalTimeDeserializer;
import org.cqq.openlibrary.common.jackson.serializer.BoxingLongArrayToStringArraySerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateTimeToStringSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalDateToStringSerializer;
import org.cqq.openlibrary.common.jackson.serializer.LocalTimeToStringSerializer;
import org.cqq.openlibrary.common.jackson.serializer.PrimitiveLongArrayToStringArraySerializer;
import org.cqq.openlibrary.common.jackson.serializer.Scale2BigDecimalToStringSerializer;

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
        serializationModule.addSerializer(Long[].class, BoxingLongArrayToStringArraySerializer.instance);
        serializationModule.addSerializer(long[].class, PrimitiveLongArrayToStringArraySerializer.instance);

        // 2. 日期序列化
        serializationModule.addSerializer(LocalTime.class, LocalTimeToStringSerializer.instance);
        serializationModule.addSerializer(LocalDate.class, LocalDateToStringSerializer.instance);
        serializationModule.addSerializer(LocalDateTime.class, LocalDateTimeToStringSerializer.instance);

        serializationModule.addDeserializer(LocalTime.class, StringToLocalTimeDeserializer.instance);
        serializationModule.addDeserializer(LocalDate.class, StringToLocalDateDeserializer.instance);
        serializationModule.addDeserializer(LocalDateTime.class, StringToLocalDateTimeDeserializer.instance);

        // 3. 浮点数
        serializationModule.addSerializer(BigDecimal.class, Scale2BigDecimalToStringSerializer.instance);
    }

    public ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(serializationModule);
        return objectMapper;
    }
}