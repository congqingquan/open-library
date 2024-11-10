package org.cqq.openlibrary.common.util;

import cn.hutool.core.util.IdUtil;

public class IdGenUtil {

    public static Long id(){
        return IdUtil.getSnowflake(1L, 1L).nextId();
    }
}
