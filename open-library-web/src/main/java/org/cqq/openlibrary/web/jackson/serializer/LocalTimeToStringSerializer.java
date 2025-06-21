package org.cqq.openlibrary.web.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.constants.Constants;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Serializer: LocalTime > String(HH_mm_ss)
 *
 * @author Qingquan
 */
public class LocalTimeToStringSerializer extends JsonSerializer<LocalTime> {

    public static final LocalTimeToStringSerializer instance = new LocalTimeToStringSerializer();

    private final static String PATTERN = Constants.HH_mm_ss;

    @Override
    public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(value)) {
            generator.writeNull();
            return;
        }
        generator.writeString(DateTimeFormatter.ofPattern(PATTERN).format(value));
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }
}
