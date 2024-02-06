package org.cqq.openlibrary.core.json;

/**
 * JSON
 *
 * @author Qingquan.Cong
 */
public interface JSON {

    default String stringify() {
        throw new UnsupportedOperationException();
    }

    default <T> T parse(String json, Class<T> typeClass) {
        throw new UnsupportedOperationException();
    }
}