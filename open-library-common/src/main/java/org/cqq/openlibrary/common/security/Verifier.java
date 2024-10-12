package org.cqq.openlibrary.common.security;

/**
 * Verifier
 *
 * @author CongQingquan
 */
public interface Verifier<Sign, Data> {
    
    boolean verify(Sign sign, Data data);
}
