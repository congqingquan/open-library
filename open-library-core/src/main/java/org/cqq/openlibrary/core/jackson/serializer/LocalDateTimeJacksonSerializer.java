package org.cqq.openlibrary.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.core.constants.Constants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Serializer: LocalDateTime > String(yyyy_MM_dd_HH_mm_ss)
 *
 * @author Qingquan.Cong
 */
public class LocalDateTimeJacksonSerializer extends JsonSerializer<LocalDateTime> {

    public static final LocalDateTimeJacksonSerializer instance = new LocalDateTimeJacksonSerializer();

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
