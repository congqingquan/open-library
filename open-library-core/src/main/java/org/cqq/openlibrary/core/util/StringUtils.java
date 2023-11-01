package org.cqq.openlibrary.core.util;

/**
 * String utils
 *
 * @author Qingquan.Cong
 */
public class StringUtils {
    
    
    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
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
}
