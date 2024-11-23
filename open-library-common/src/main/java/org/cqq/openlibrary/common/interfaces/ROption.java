package org.cqq.openlibrary.common.interfaces;

/**
 * Response option
 *
 * @author Qingquan
 */
public interface ROption {
    
    ROption SUCCESS = new ROption() {
        @Override
        public Long getCode() {
            return 0L;
        }
        
        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };
    
    Long getCode();
    
    String getMessage();
}
