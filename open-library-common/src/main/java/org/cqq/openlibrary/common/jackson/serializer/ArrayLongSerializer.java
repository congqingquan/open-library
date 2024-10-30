package org.cqq.openlibrary.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.util.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Serializer: Long[] > String[]
 *
 * @author Qingquan
 */
public class ArrayLongSerializer extends JsonSerializer<Long[]> {

    public static final ArrayLongSerializer instance = new ArrayLongSerializer();

    @Override
    public void serialize(Long[] values, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (ArrayUtils.isEmpty(values)) {
            generator.writeNull();
            return;
        }
        String[] newValues = Arrays.stream(values).map(Object::toString).toArray(String[]::new);
        generator.writeArray(newValues, 0, newValues.length);
    }

    @Override
    public Class<Long[]> handledType() {
        return Long[].class;
    }
}