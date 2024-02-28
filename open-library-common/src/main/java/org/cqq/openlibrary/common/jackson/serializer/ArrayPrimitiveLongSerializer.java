package org.cqq.openlibrary.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.util.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;

/**
 * Serializer: long[] > String[]
 *
 * @author Qingquan.Cong
 */
public class ArrayPrimitiveLongSerializer extends JsonSerializer<long[]> {

    public static final ArrayPrimitiveLongSerializer instance = new ArrayPrimitiveLongSerializer();

    @Override
    public void serialize(long[] values, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (ArrayUtils.isEmpty(values)) {
            generator.writeNull();
            return;
        }
        String[] newValues = Arrays.stream(values).mapToObj(String::valueOf).toArray(String[]::new);
        generator.writeArray(newValues, 0, newValues.length);
    }

    @Override
    public Class<long[]> handledType() {
        return long[].class;
    }
}