import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.cqq.openlibrary.common.util.JSONUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author Qingquan
 */
public class JSONUtilsTest {
    
    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private Integer age;
    }
    
    @Test
    public void isJSONTest() {
        String jsonString = JSONUtils.toJSONString(Map.of("name", "CQQ", "age", 25, "gender", "MAN"));
        System.out.println(JSONUtils.isJSON(jsonString));
    }
    
    @Test
    public void parse() {
        // Object
        String jsonString = JSONUtils.toJSONString(Map.of("name", "CQQ", "age", 25, "gender", "MAN"));
        
        Map<String, ?> map = JSONUtils.parse2Map(jsonString);
        System.out.println(map);
        
        User user = JSONUtils.parse(jsonString, User.class);
        System.out.println(user);
        
        user = JSONUtils.parse(jsonString, new TypeReference<>() {
        });
        System.out.println(user);
        
        // Array
        String jsonArrayString = JSONUtils.toJSONString(List.of(
                new User("ZhangSan", 20),
                new User("Lisi", 25)
        ));

        List<User> users = JSONUtils.parse(jsonArrayString, new TypeReference<>() {
        });
        System.out.println(users);
        
        // Error
        String jsonStr = "";
        System.out.println(JSONUtils.isJSON(jsonStr));
        Map<String, ?> stringMap = JSONUtils.parse2Map(jsonStr);
        System.out.println(stringMap);
    }
}
