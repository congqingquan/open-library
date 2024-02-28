package org.cqq.openlibrary.common.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Serializer: BigDecimal(scale: 2, RoundingMode.DOWN).toPlainString() > String
 *
 * @author Qingquan.Cong
 */
public class Reserve2BigDecimalsJacksonSerializer extends JsonSerializer<BigDecimal> {

    public static final Reserve2BigDecimalsJacksonSerializer instance = new Reserve2BigDecimalsJacksonSerializer();

    @Override
    public void serialize(BigDecimal value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        if (Objects.isNull(value)) {
            generator.writeNull();
            return;
        }
        generator.writeString(value.setScale(2, RoundingMode.DOWN).toPlainString());
    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }
}
