package org.cqq.openlibrary.common.interfaces;

/**
 * Name
 *
 * @author Qingquan
 */
public interface Name {

    default String name() {
        return this.getClass().getSimpleName();
    }
}