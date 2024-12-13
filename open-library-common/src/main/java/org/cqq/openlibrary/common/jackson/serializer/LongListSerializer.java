package org.cqq.openlibrary.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cqq.openlibrary.common.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

/**
 * {@literal Serializer: List<Long> > String[]}
 *
 * @author Qingquan
 */
public class LongListSerializer extends JsonSerializer<List<Long>> {
    
    public static final LongListSerializer instance = new LongListSerializer();
    
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