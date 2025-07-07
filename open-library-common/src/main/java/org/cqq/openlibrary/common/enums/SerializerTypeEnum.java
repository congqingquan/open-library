package org.cqq.openlibrary.common.enums;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
 * Serializer type enum
 *
 * @author Qingquan
 */
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
