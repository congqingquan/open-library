package org.cqq.openlibrary.common.enums;

import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalDateDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalDateTimeDeserializer;
import org.cqq.openlibrary.common.jackson.deserializer.StringToLocalTimeDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Deserializer type enum
 *
 * @author Qingquan
 */
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