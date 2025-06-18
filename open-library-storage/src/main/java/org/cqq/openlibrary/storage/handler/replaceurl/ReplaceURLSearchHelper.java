package org.cqq.openlibrary.storage.handler.replaceurl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cqq.openlibrary.storage.annotation.ReplaceURL;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Replace url search helper
 *
 * @author Qingquan
 */
public class ReplaceURLSearchHelper {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplaceURLDummyNode {
        
        @ReplaceURL
        private Object value;
    }
    
    @FunctionalInterface
    public interface ReplaceURLAction {
        
        String replace(ReplaceURL annotation, String url);
    }
    
    public static Object replaceObjectFieldURL(Object object,
                                               ReplaceURLAction replaceURLAction) {
        ReplaceURLDummyNode replaceURLWrapper = new ReplaceURLDummyNode(object);
        doReplaceObjectFieldURL(replaceURLWrapper, replaceURLAction);
        return replaceURLWrapper.getValue();
    }
    
    @SuppressWarnings("unchecked")
    public static void doReplaceObjectFieldURL(Object object,
                                               ReplaceURLAction replaceURLAction) {
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            
            // Check null
            field.setAccessible(true);
            Object fieldValue = ReflectionUtils.getField(field, object);
            if (fieldValue == null) {
                return;
            }
            ReplaceURL annotation = field.getAnnotation(ReplaceURL.class);
            
            // Multiple value:
            // case1. Object[ ..., "http://xxx.png", "http://xxx.png" ,... ]
            if (fieldValue instanceof Object[] dataArr) {
                for (int i = 0; i < dataArr.length; i++) {
                    if (dataArr[i] instanceof String string) {
                        dataArr[i] = replaceURLAction.replace(annotation, string);
                    } else {
                        doReplaceObjectFieldURL(dataArr[i], replaceURLAction);
                    }
                }
            }
            // case2. Collection<[ ..., "http://xxx.png", "http://xxx.png" ,... ]>
            // case3. Collection<[ ..., { @ReplaceURL Object field }, ... ]>
            else if (fieldValue instanceof Collection) {
                Collection<Object> dataColl = (Collection<Object>) fieldValue;
                Collection<Object> dataCollNew = doReplaceCollectionURL(dataColl, annotation, replaceURLAction, ArrayList::new);
                ReflectionUtils.setField(field, object, dataCollNew);
            }
            // case4. IPage
            else if (fieldValue instanceof IPage) {
                IPage<Object> pages = (IPage<Object>) fieldValue;
                List<Object> newPages = doReplaceCollectionURL(pages.getRecords(), annotation, replaceURLAction, ArrayList::new);
                pages.setRecords(newPages);
            }
            
            // Single value
            // case1. String value:
            else if (fieldValue instanceof String string) {
                String url = replaceURLAction.replace(annotation, string);
                ReflectionUtils.setField(field, object, url);
            }
            // case2. Other object
            else {
                doReplaceObjectFieldURL(fieldValue, replaceURLAction);
            }
        }, field -> field.isAnnotationPresent(ReplaceURL.class));
    }
    
    private static <C extends Collection<Object>> C doReplaceCollectionURL(Collection<?> dataColl,
                                                                           ReplaceURL annotation,
                                                                           ReplaceURLAction replaceURLAction,
                                                                           Supplier<C> container) {
        C dataCollNew = container.get();
        List<?> dataList = new ArrayList<>(dataColl);
        for (Object data : dataList) {
            if (data instanceof String string) {
                dataCollNew.add(replaceURLAction.replace(annotation, string));
            } else {
                doReplaceObjectFieldURL(data, replaceURLAction);
                dataCollNew.add(data);
            }
        }
        return dataCollNew;
    }
}
