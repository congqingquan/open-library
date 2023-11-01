package org.cqq.oplibrary.web.entity;

/**
 * Response option
 *
 * @author Qingquan.Cong
 */
public interface ROption {
    
    ROption SUCCESS = new ROption() {
        @Override
        public Integer getCode() {
            return 200;
        }
        
        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };
    
    Integer getCode();
    
    String getMessage();
}
