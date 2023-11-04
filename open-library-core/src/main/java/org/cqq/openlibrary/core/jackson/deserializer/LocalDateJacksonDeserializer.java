package org.cqq.openlibrary.core.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.core.constants.Constants;
import org.cqq.openlibrary.core.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer: String(yyyy_MM_dd) > LocalDate
 *
 * @author Qingquan.Cong
 */
public class LocalDateJacksonDeserializer extends JsonDeserializer<LocalDate> {

    public static final LocalDateJacksonDeserializer instance = new LocalDateJacksonDeserializer();

    private final static String PATTERN = Constants.yyyy_MM_dd;

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String originDate = parser.getText();
        if (StringUtils.isBlank(originDate)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN);
        return LocalDate.parse(originDate, dateTimeFormatter);
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }
}
