package org.cqq.openlibrary.persistence.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Page param (Work on mybatis plus)
 *
 * @author Qingquan.Cong
 */
@Data
public class MPPageParam<T> {

    private Long pageNo;

    private Long pageSize;

    public IPage<T> iPage() {
        Page<T> page = new Page<>();
        if (pageNo == null || pageSize == null) {
            page.setSize(-1L);
        } else {
            page.setCurrent(pageNo);
            page.setSize(pageSize);
        }
        return page;
    }
}