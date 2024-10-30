package org.cqq.openlibrary.common.security;

/**
 * Digester
 *
 * @author Qingquan
 */
public interface Digester<IN_DATA, OUT_DATA> {
    
    OUT_DATA digest(IN_DATA data);
}
