package org.cqq.openlibrary.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex utils
 *
 * @author Qingquan
 */
public class RegexUtils {
    
    private RegexUtils() {}
    
    private static final Pattern EXTRA_IMG_SRC_PATTERN = Pattern.compile("<img +src=\"([^\"]*)\"[^>]*>");
    
    @FunctionalInterface
    public interface ReplaceSrcAction {
        
        String replace(String imgTag, String imgSrc);
    }
    
    public static String replaceSrc(String content, ReplaceSrcAction replaceAction) {
        Matcher matcher = EXTRA_IMG_SRC_PATTERN.matcher(content);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String imgTag = matcher.group(0);
            String imgSrc = matcher.group(1);
            matcher.appendReplacement(builder, replaceAction.replace(imgTag, imgSrc));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
}
