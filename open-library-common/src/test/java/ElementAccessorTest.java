import lombok.Data;
import org.cqq.openlibrary.common.component.elementaccessor.CollectionElementAccessor;
import org.cqq.openlibrary.common.component.elementaccessor.ElementAccessor;
import org.cqq.openlibrary.common.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Qingquan
 */
public class ElementAccessorTest {
    
    @Data
    public static class AccessData {
        
        private String str;
        
        private Collection<String> collection;
        
        private Collection<Collection<String>> nestedCollection;
    }
    
    public static void main(String[] args) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        
        AccessData accessData = new AccessData();
        accessData.setStr("str");
        accessData.setCollection(CollectionUtils.newArrayList("c1", "c2"));
        accessData.setNestedCollection(List.of(
                List.of("nc1", "nc2"),
                List.of("nc3", "nc4")
        ));
        
        ElementAccessor.access(
                List.of(accessData),
                List.of(
                        new CollectionElementAccessor(collection -> {
                            System.out.println("Consume -> " + collection.stream().map(Object::toString).collect(Collectors.joining(",")));
                            return true;
                        })
                ));
    }
}
