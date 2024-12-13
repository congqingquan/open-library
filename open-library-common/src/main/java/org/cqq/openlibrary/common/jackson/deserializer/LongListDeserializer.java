package org.cqq.openlibrary.common.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@literal Deserializer: String[] > List<Long>}
 *
 * @author Qingquan
 */
public class LongListDeserializer extends JsonDeserializer<List<Long>> {
    
    public static final LongListDeserializer instance = new LongListDeserializer();
    
    @Override
    public List<Long> deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        List<Long> longList = new ArrayList<>();
        JsonNode node = parser.getCodec().readTree(parser);
        for (JsonNode jsonNode : node) {
            longList.add(jsonNode.asLong());
        }
        return longList;
    }
}