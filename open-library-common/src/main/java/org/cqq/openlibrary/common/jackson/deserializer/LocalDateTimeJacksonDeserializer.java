package org.cqq.openlibrary.common.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Deserializer: String(yyyy_MM_dd_HH_mm_ss) > LocalDateTime / String(yyyy_MM_dd_HH_mm_ss_SSS) > LocalDateTime
 *
 * @author Qingquan.Cong
 */
public class LocalDateTimeJacksonDeserializer extends JsonDeserializer<LocalDateTime> {

    public static final LocalDateTimeJacksonDeserializer instance = new LocalDateTimeJacksonDeserializer();

    private final static String PATTERN_1 = Constants.yyyy_MM_dd_HH_mm_ss;
    private final static String PATTERN_2 = Constants.yyyy_MM_dd_HH_mm_ss_SSS;

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String originDate = parser.getText();
        if (StringUtils.isBlank(originDate)) {
            return null;
        }
        LocalDateTime targetDate;
        if (originDate.contains("T")) {
            targetDate = LocalDateTime.parse(originDate);
        } else if (originDate.length() == 23) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_2);
            targetDate = LocalDateTime.parse(originDate, dateTimeFormatter);
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_1);
            targetDate = LocalDateTime.parse(originDate, dateTimeFormatter);
        }
        return targetDate;
    }


    @Override
    public Class<?> handledType() {
        return LocalDateTime.class;
    }
}
