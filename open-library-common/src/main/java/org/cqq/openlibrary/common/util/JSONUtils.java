package org.cqq.openlibrary.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.func.checked.CheckedFunction;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.SimpleTimeZone;
import java.util.function.Supplier;

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
            throw new RuntimeException("Parse json string error", t);
        }
    }
    
    public static boolean isJson(String jsonObject) {
        return isJson(jsonObject, Object.class);
    }
    
    public static <T> boolean isJson(String jsonObject, Class<T> objectType) {
        try {
            callMapper(mapper -> mapper.readValue(jsonObject, objectType));
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
    
    public static <T> T parseObject(String jsonObject, Class<T> objectType) {
        return callMapper(mapper -> mapper.readValue(jsonObject, objectType));
    }
    
    @SuppressWarnings({"unchecked"})
    public static <T> T parseObject(String jsonObject, T result) {
        // 不能使用 TypeReference 的原因：无法在编译期间得知泛型类型的具体类型，运行时实际采用的类型为 LinkedHashMap，所以 ReturnableBeanUtils 一定无法正确 copy 属性
        // T source = callMapper(mapper -> mapper.readValue(jsonObject, new TypeReference<>() {}));
        
        T source = callMapper(mapper -> mapper.readValue(jsonObject, (Class<? extends T>) result.getClass()));
        return ReturnableBeanUtils.copyProperties(source, result);
    }
    
    public static <T> T parseObject(String jsonObject, Supplier<T> objectSupplier) {
        return parseObject(jsonObject, objectSupplier.get());
    }
    
    public static <T> T parseObject(String jsonObject, TypeReference<T> objectType) {
        return callMapper(mapper -> mapper.readValue(jsonObject, objectType));
    }
    
    public static <T extends Collection<E>, E> T parseArray(String jsonArray, Supplier<T> collectorSupplier) {
        T collector = collectorSupplier.get();
        try {
            // 为何不使用 collector.addAll(callMapper(m -> m.readValue(jsonArray, new TypeReference<T>() {}))) ?
            // 因反射无法获取lambda体中的范型类型.
            collector.addAll(MAPPER.readValue(jsonArray, new TypeReference<T>() {
            }));
        } catch (IOException e) {
            throw new RuntimeException("Parse json array string error", e);
        }
        return collector;
    }
    
    public static String toJSONString(Object obj) {
        return callMapper(mapper -> mapper.writeValueAsString(obj));
    }
}