package org.cqq.openlibrary.core.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.core.constants.Constants;
import org.cqq.openlibrary.core.util.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer: String(HH_mm_ss) > LocalTime
 *
 * @author Qingquan.Cong
 */
public class LocalTimeJacksonDeserializer extends JsonDeserializer<LocalTime> {

    public static final LocalTimeJacksonDeserializer instance = new LocalTimeJacksonDeserializer();

    private final static String PATTERN = Constants.HH_mm_ss;

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        String originDate = p.getText();
        if (StringUtils.isBlank(originDate)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
        return LocalTime.parse(originDate, dateTimeFormatter);
    }

    @Override
    public Class<LocalTime> handledType() {
        return LocalTime.class;
    }
}
