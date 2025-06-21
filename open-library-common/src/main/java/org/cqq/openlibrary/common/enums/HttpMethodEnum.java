package org.cqq.openlibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Http method enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum HttpMethodEnum {
    
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
}
