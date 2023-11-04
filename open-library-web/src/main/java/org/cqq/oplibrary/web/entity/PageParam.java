package org.cqq.oplibrary.web.entity;

import lombok.Data;

/**
 * Page param
 *
 * @author Qingquan.Cong
 */
@Data
public abstract class PageParam {

    protected Long pageNo;

    protected Long pageSize;
}