package org.cqq.openlibrary.core.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.cqq.openlibrary.core.constants.Constants;
import org.cqq.openlibrary.core.func.CheckedFunction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.SimpleTimeZone;
import java.util.function.Supplier;

/**
 * JSON utils
 *
 * @author Qingquan.Cong
 */
public class JSONUtils {

    private JSONUtils() {
    }

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules()
                .setTimeZone(SimpleTimeZone.getTimeZone(Constants.TIME_ZONE_GMT_8))
                .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .setDateFormat(new SimpleDateFormat(Constants.yyyy_MM_dd_HH_mm_ss));
    }

    private static <R> R callMapper(CheckedFunction<ObjectMapper, R> function) {
        try {
            return function.apply(mapper);
        } catch (Throwable t) {
            throw new RuntimeException("Parse json string error", t);
        }
    }

    public static <T> T parseObject(String jsonObject, Class<T> objectType) {
        return callMapper(m -> m.readValue(jsonObject, objectType));
    }

    public static <T extends Collection<E>, E> T parseArray(String jsonArray, Supplier<T> collectorSupplier) {
        T collector = collectorSupplier.get();
        try {
            // 为何不使用 collector.addAll(callMapper(m -> m.readValue(jsonArray, new TypeReference<T>() {}))) ?
            // 因反射无法获取lambda体中的范型类型.
            collector.addAll(mapper.readValue(jsonArray, new TypeReference<T>() {}));
        } catch (IOException e) {
            throw new RuntimeException("Parse json array string error", e);
        }
        return collector;
    }

    public static String toJsonString(Object obj) {
        return callMapper(m -> m.writeValueAsString(obj));
    }
}