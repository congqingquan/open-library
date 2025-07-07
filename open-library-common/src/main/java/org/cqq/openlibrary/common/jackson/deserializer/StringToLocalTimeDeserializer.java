package org.cqq.openlibrary.common.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.util.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer: String(HH_mm_ss) > LocalTime
 *
 * @author Qingquan
 */
public class StringToLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    public static final StringToLocalTimeDeserializer instance = new StringToLocalTimeDeserializer();

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
