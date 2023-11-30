package org.cqq.openlibrary.core.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.core.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Deserializer: String(yyyy_MM_dd) > LocalDate
 *
 * @author Qingquan.Cong
 */
public class Reserve2BigDecimalsJacksonDeserializer extends JsonDeserializer<BigDecimal> {

    public static final Reserve2BigDecimalsJacksonDeserializer instance = new Reserve2BigDecimalsJacksonDeserializer();

    @Override
    public BigDecimal deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String decimal = parser.getText();
        if (StringUtils.isBlank(decimal)) {
            return null;
        }
        return new BigDecimal(decimal).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public Class<BigDecimal> handledType() {
        return BigDecimal.class;
    }
}
