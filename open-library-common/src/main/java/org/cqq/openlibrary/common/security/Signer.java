package org.cqq.openlibrary.common.security;

/**
 * Signer
 *
 * @author CongQingquan
 */
public interface Signer<Data, Sign> {
    
    Sign sign(Data data);
}
