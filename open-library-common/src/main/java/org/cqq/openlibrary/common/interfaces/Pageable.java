package org.cqq.openlibrary.common.interfaces;

/**
 * Pageable
 *
 * @author Qingquan.Cong
 */
public interface Pageable<T extends Number> {

    void setPageNo(T pageNo);

    void setPageSize(T pageSize);

    T getPageNo();

    T getPageSize();
}