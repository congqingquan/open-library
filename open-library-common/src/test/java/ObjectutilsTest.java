import org.cqq.openlibrary.common.util.ObjectUtils;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ObjectUtils test
 *
 * @author Qingquan
 */
public class ObjectutilsTest {
    
    @Test
    public void areEmptyTest() {
        Object obj = null;
//        Object obj = new Object();
        
        Optional<Object> optional = Optional.empty();
//        Optional<Object> optional = Optional.of(1);
        
        String str = "";
//        String str = "A";
        
        Object[] objs = new Object[]{};
//        Object[] objs = new Object[]{ new Object() };
        
        Collection<?> collection = List.of();
//        Collection<?> collection = List.of(1);
        
        Map<?, ?> map = Map.of();
//        Map<?, ?> map = Map.of("K", "V");
        
        System.out.println(ObjectUtils.areEmpty(obj, optional, str, objs, collection, map));
    }
    
    @Test
    public void areNotEmptyTest() {
//        Object obj = null;
        Object obj = new Object();

//        Optional<Object> optional = Optional.empty();
        Optional<Object> optional = Optional.of(1);

//        String str = "";
        String str = "A";

//        Object[] objs = new Object[]{};
        Object[] objs = new Object[]{ new Object() };

//        Collection<?> collection = List.of();
        Collection<?> collection = List.of(1);

//        Map<?, ?> map = Map.of();
        Map<?, ?> map = Map.of("K", "V");
        
        System.out.println(ObjectUtils.areNotEmpty(obj, optional, str, objs, collection, map));
    }
}
