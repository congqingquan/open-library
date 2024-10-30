package org.cqq.openlibrary.common.component.checkfield;

import org.cqq.openlibrary.common.annotation.Flag;
import org.cqq.openlibrary.common.annotation.Ignore;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Default field checker
 *
 * @author Qingquan
 */
@Component
public class DefaultModifiedFieldChecker extends ModifiedFieldChecker {

    public DefaultModifiedFieldChecker() {
        super(Flag.class, Ignore.class);
    }

    @Override
    protected boolean compareCollection(Collection<?> sourceFieldVal, Collection<?> checkFieldVal) {
        return compareObject(sourceFieldVal, checkFieldVal);
    }

    @Override
    protected boolean compareMap(Map<?, ?> sourceFieldVal, Map<?, ?> checkFieldVal) {
        return compareObject(sourceFieldVal, checkFieldVal);
    }
}