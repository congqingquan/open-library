package org.cqq.openlibrary.common.security;

/**
 * Verifier
 *
 * @author Qingquan
 */
public interface Verifier<Sign, Data> {
    
    boolean verify(Sign sign, Data data);
}
