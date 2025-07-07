package org.cqq.openlibrary.common.jackson.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.constants.Constants;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Serializer: LocalDate > String(yyyy_MM_dd)
 *
 * @author Qingquan
 */
public class LocalDateToStringSerializer extends JsonSerializer<LocalDate> {

    public static final LocalDateToStringSerializer instance = new LocalDateToStringSerializer();

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
