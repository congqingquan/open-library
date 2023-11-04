package org.cqq.openlibrary.core.jackson.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.core.constants.Constants;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Serializer: LocalDate > String(yyyy_MM_dd)
 *
 * @author Qingquan.Cong
 */
public class LocalDateJacksonSerializer extends JsonSerializer<LocalDate> {

    public static final LocalDateJacksonSerializer instance = new LocalDateJacksonSerializer();

    private final static String PATTERN = Constants.yyyy_MM_dd;

    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(value)) {
            generator.writeNull();
            return;
        }
        generator.writeString(DateTimeFormatter.ofPattern(PATTERN).format(value));
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }
}
