package org.cqq.openlibrary.common.util;

import org.springframework.beans.BeanUtils;

/**
 * Returnable bean utils
 *
 * @author Qingquan.Cong
 */
public class ReturnableBeanUtils {

    private ReturnableBeanUtils() {}

    public static <S, T> T copyProperties(S source, T target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }
}