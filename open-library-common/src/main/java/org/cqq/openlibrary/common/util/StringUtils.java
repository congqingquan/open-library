package org.cqq.openlibrary.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * String utils
 *
 * @author Qingquan
 */
public class StringUtils {
    
    private StringUtils() {
    }
    
    public static int length(CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }
    
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }
    
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }
    
    public static boolean isBlank(CharSequence cs) {
        final int len = length(cs);
        if (len == 0) {
            return true;
        }
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    
    private String replaceMiddleChars(String str, int length, char replaceChar) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        if (length > str.length()) {
            length = str.length();
        }
        int midStartIndex = (str.length() >> 1) - (length >> 1);
        int midEndIndex = midStartIndex + length - 1;
        
        char[] chars = str.toCharArray();
        for (int i = midStartIndex; i <= midEndIndex; i++) {
            chars[i] = replaceChar;
        }
        return new String(chars);
    }
    
    public static <R> List<R> split(String str, String separator,
                                    Function<String, R> mapping, Consumer<R> consumer) {
        if (isBlank(str)) {
            return new ArrayList<>();
        }
        if (mapping == null) {
            throw new IllegalArgumentException("Mapping function cannot be null");
        }
        
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer function cannot be null");
        }
        List<R> result = new ArrayList<>();
        if (null == separator) {
            result.add(mapping.apply(str));
        } else {
            String[] splitArray = str.split(separator);
            for (String s : splitArray) {
                result.add(mapping.apply(s));
            }
        }
        return result;
    }
    
    public static String join(String delimiter, Predicate<CharSequence> filter, CharSequence...  elements) {
        return Arrays.stream(elements).filter(filter).collect(Collectors.joining(delimiter));
    }
    
    public static String firstLetterToLowerCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return str;
        }
        char[] arr = str.toCharArray();
        arr[0] = Character.toLowerCase(firstChar);
        return new String(arr);
    }
    
    public static String firstLetterToUpperCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return str;
        }
        char[] arr = str.toCharArray();
        arr[0] = Character.toUpperCase(firstChar);
        return new String(arr);
    }
    
    public static String appendPrefix(String str, String prefix, Boolean directly) {
        if (isBlank(str)) {
            return str;
        }
        if (isBlank(prefix)) {
            return str;
        }
        if (str.startsWith(prefix)) {
            return directly ? prefix + str : str;
        }
        return str + prefix;
    }
    
    public static String appendSuffix(String str, String suffix, Boolean directly) {
        if (isBlank(str)) {
            return str;
        }
        if (isBlank(suffix)) {
            return str;
        }
        if (str.endsWith(suffix)) {
            return directly ? str + suffix : str;
        }
        return str + suffix;
    }
}