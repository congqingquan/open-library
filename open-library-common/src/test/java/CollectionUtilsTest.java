import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.cqq.openlibrary.common.util.CollectionUtils.newArrayList;

public class CollectionUtilsTest {
    
    @Test
    public void setTest() {
        System.out.println(CollectionUtils.intersectionSet(Arrays.asList(1, 2), Arrays.asList(2, 4), Function.identity(), ArrayList::new));
        System.out.println(CollectionUtils.differenceSet(Arrays.asList(1, 2), Arrays.asList(2, 4), Function.identity(), false, ArrayList::new));
        System.out.println(CollectionUtils.differenceSet(Arrays.asList(1, 2), Arrays.asList(2, 4), Function.identity(), true, ArrayList::new));
        System.out.println(CollectionUtils.unionSet(Arrays.asList(1, 2), Arrays.asList(2, 4), ArrayList::new));
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class DeepForeachNode {
        private String id;
        private List<DeepForeachNode> children;
    }
    
    @Test
    public void deepForeachTest() {
        DeepForeachNode deepForeachNode = new DeepForeachNode("A", Arrays.asList(
                new DeepForeachNode("A1", Arrays.asList(
                        new DeepForeachNode("A11", null),
                        new DeepForeachNode("A12", null)
                )),
                new DeepForeachNode("A2", null)
        ));
        
        CollectionUtils.deepForeach(Collections.singleton(deepForeachNode), DeepForeachNode::getChildren, (path, element) -> {
            System.out.println();
        });
    }
    
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    static class CompareData {
        private String id;
        private String name;
    }
    
    @Test
    public void compareTest() {
        List<CompareData> oldEls = List.of(
                new CompareData("1", "张三"),
                new CompareData("2", "李四"),
                new CompareData("3", "王五"),
                new CompareData("3", "王五_v2")
        );
        List<CompareData> newEls = List.of(
                new CompareData("3", "王五_upd"),
                new CompareData("4", "赵六")
        );
        
        var compare = CollectionUtils.compare(
                oldEls,
                newEls,
                (oldElement, newElement) -> oldElement.getId().equals(newElement.getId())
        );
        System.out.println(compare);
    }
    
    @Test
    public void cartesianProductTest() {
        System.out.println(
                CollectionUtils.cartesianProduct(
                        newArrayList(
                                newArrayList("A1", "A2"),
                                null,
                                newArrayList("B1", "B2"),
                                newArrayList("C1", "C2")
                        )
                )
        );
        
        System.out.println(
                CollectionUtils.cartesianProduct(
                        newArrayList(
                                newArrayList("A1", "A2"),
                                null,
                                newArrayList("B1", "B2", null)
                        )
                )
        );
    }
}