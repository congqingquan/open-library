package org.cqq.openlibrary.common.interfaces;

/**
 * JSON
 *
 * @author Qingquan
 */
public interface JSON {

    default String stringify() {
        throw new UnsupportedOperationException();
    }

    default <T> T parse(String json, Class<T> typeClass) {
        throw new UnsupportedOperationException();
    }
}