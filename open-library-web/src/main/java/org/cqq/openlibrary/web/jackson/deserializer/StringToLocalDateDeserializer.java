package org.cqq.openlibrary.web.jackson.deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer: String(yyyy_MM_dd) > LocalDate
 *
 * @author Qingquan
 */
public class StringToLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    public static final StringToLocalDateDeserializer instance = new StringToLocalDateDeserializer();

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
