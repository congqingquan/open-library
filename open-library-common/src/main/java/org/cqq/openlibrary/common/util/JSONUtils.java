package org.cqq.openlibrary.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.SimpleTimeZone;

/**
 * JSON utils
 *
 * @author Qingquan
 */
public class JSONUtils {
    
    private JSONUtils() {
    }
    
    private static final ObjectMapper MAPPER;
    
    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules()
                .setTimeZone(SimpleTimeZone.getTimeZone(Constants.TIME_ZONE_GMT_8))
                .setDateFormat(new SimpleDateFormat(Constants.yyyy_MM_dd_HH_mm_ss))
                // serialization
                .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                // deserialization
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    private static <R, X extends Throwable> R callMapper(CheckedFunction<ObjectMapper, R, X> function) {
        try {
            return function.apply(MAPPER);
        } catch (Throwable t) {
            throw new IllegalArgumentException("Parse json string error", t);
        }
    }
    
    // ===================================== Parse =====================================
    
    public static <T> T parse(String jsonObject, Class<? extends T> objectType) {
        return callMapper(mapper -> mapper.readValue(jsonObject, objectType));
    }
    
    public static <T> T parse(String jsonObject, TypeReference<T> objectType) {
        return callMapper(mapper -> mapper.readValue(jsonObject, objectType));
    }
    
    public static <V> Map<String, V> parse2Map(String json) {
        return parse(json, new TypeReference<>() {
        });
    }
    
    // ===================================== Other =====================================
    
    public static boolean isJSON(String json) {
        try {
            callMapper(mapper -> mapper.readValue(json, Object.class));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    
    public static String toJSONString(Object obj) {
        return callMapper(mapper -> mapper.writeValueAsString(obj));
    }
}