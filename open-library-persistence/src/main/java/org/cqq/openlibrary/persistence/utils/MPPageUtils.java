package org.cqq.openlibrary.persistence.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cqq.openlibrary.core.util.BeanUtils;

import java.util.List;
import java.util.function.Supplier;

/**
 * Page utils (Work on Mybatis plus)
 *
 * @author Qingquan.Cong
 */
public class MPPageUtils {

    private MPPageUtils() {}

    public static <T> IPage<T> convertRecordType(IPage<?> page, Supplier<T> convertSupplier) {
        Page<T> returnPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal(), page.isSearchCount());
        List<T> records = BeanUtils.copy2ArrayList(page.getRecords(), convertSupplier);
        returnPage.setRecords(records);
        return returnPage;
    }
}