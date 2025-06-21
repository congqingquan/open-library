package org.cqq.openlibrary.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Media type enum
 *
 * @author Qingquan
 */
@Getter
@AllArgsConstructor
public enum MediaTypeEnum {
    
    ALL("*/*"),
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_XML("text/xml"),
    TEXT_CSV("text/csv"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_GIF("image/gif"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    ;
    
    private final String value;
}
