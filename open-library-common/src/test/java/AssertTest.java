import org.cqq.openlibrary.common.util.Assert;
import org.junit.jupiter.api.Test;

public class AssertTest {
    
    @Test
    public void test() {
//        Assert.notBlank( " ", "Str cannot be blank");
//        Assert.hasLength( "", "Str must has length");

//        Assert.hasLength( "", 1, "Str length must be 1");
//        Assert.hasLength( "AA", 1, "Str length must be 1");
//        Assert.hasLength( "A", 1, "Str length must be 1");

//        Assert.hasLength( "", 1, 5, "Str length must between [1,5]");
//        Assert.hasLength( "AA", 1, 5, "Str length must between [1,5]");
//        Assert.hasLength( "AAAAAA", 1, 5, "Str length must between [1,5]");
        
        Assert.notNull(new Object(), "Object cannot be null");
        Assert.notNull(null, "Object cannot be null");
    }
}
