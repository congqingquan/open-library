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
    public void  deepForeachTest() {
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
}