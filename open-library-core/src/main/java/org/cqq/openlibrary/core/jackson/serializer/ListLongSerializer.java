package org.cqq.openlibrary.core.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.core.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * Serializer: List<Long> > String[]
 *
 * @author Qingquan.Cong
 */
public class ListLongSerializer extends JsonSerializer<List<Long>> {

    public static final ListLongSerializer instance = new ListLongSerializer();

    @Override
    public void serialize(List<Long> values, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (CollectionUtils.isEmpty(values)) {
            generator.writeNull();
            return;
        }
        String[] newValues = values.stream().map(Object::toString).toArray(String[]::new);
        generator.writeArray(newValues, 0, newValues.length);
    }
}