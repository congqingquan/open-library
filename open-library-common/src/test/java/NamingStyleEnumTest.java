import org.cqq.openlibrary.common.enums.NamingStyleEnum;
import org.junit.jupiter.api.Test;

public class NamingStyleEnumTest {
    
    @Test
    public void test() {
        String name = "helloWorld";
        
        System.out.println(NamingStyleEnum.detectStyle(name).orElse(null));
        System.out.println(NamingStyleEnum.change2(NamingStyleEnum.LOWER_UNDERSCORE, name));
    }
}
