import org.cqq.openlibrary.common.util.WeightRandom;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Qingquan
 */
public class WeightRandomTest {
    
    @Test
    public void test() {
        LinkedHashMap<Double, String> map = new LinkedHashMap<>();
        map.put(1.0, "A");
        map.put(39.0, "B");
        map.put(60.0, "C");
        
        WeightRandom<String> wr = WeightRandom.create(map, false);
        
        int total = 100_000;
        Map<String, Integer> counter = new HashMap<>();
        for (int i = 0; i < total; i++) {
            String d = wr.get();
            if (d == null) {
                System.err.println("1111");
            }
            counter.compute(d, (key, val) ->  {
                return val == null ? 1 : ++val;
            });
        }
        
        AtomicInteger tc = new AtomicInteger();
        counter.forEach((k, v) -> {
            tc.addAndGet(v);
            System.out.println(k + " > " + ((double) v / total));
        });
        
        System.out.println(tc.get());
    }
}
