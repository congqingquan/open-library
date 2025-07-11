package org.cqq.openlibrary.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.constants.Constants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Serializer: LocalDateTime > String(yyyy_MM_dd_HH_mm_ss)
 *
 * @author Qingquan
 */
public class LocalDateTimeToStringSerializer extends JsonSerializer<LocalDateTime> {

    public static final LocalDateTimeToStringSerializer instance = new LocalDateTimeToStringSerializer();

    private final static String PATTERN = Constants.yyyy_MM_dd_HH_mm_ss;

    @Override
    public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(value)) {
            generator.writeNull();
            return;
        }
        generator.writeString(DateTimeFormatter.ofPattern(PATTERN).format(value));
    }

    @Override
    public Class<LocalDateTime> handledType() {
        return LocalDateTime.class;
    }
}
