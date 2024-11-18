package org.cqq.openlibrary.common.interfaces;

/**
 * Response option
 *
 * @author Qingquan
 */
public interface ROption {
    
    ROption SUCCESS = new ROption() {
        @Override
        public Integer getCode() {
            return 0;
        }
        
        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };
    
    Integer getCode();
    
    String getMessage();
}
