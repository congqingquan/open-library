package org.cqq.openlibrary.common.security;

/**
 * Digester
 *
 * @author CongQingquan
 */
public interface Digester<IN_DATA, OUT_DATA> {
    
    OUT_DATA digest(IN_DATA data);
}
