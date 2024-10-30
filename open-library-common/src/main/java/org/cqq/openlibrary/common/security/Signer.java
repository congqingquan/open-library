package org.cqq.openlibrary.common.security;

/**
 * Signer
 *
 * @author Qingquan
 */
public interface Signer<Data, Sign> {
    
    Sign sign(Data data);
}
