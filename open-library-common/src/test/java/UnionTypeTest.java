import org.cqq.openlibrary.common.types.union.Union2Type;
import org.cqq.openlibrary.common.types.union.Union3Type;

/**
 * @author Qingquan
 */
public class UnionTypeTest {
    
    public static void main(String[] args) {
        Union2Type<String, Demo> union2Type = new Union2Type<>(new Demo()) {
        };
        union2TypeTest(union2Type);
//        union2Type.toT1();
        union2Type.toT2();
        
        Union3Type<String, Demo, Number> union3Type = new Union3Type<>(new Demo()) {
        };
        union3TypeTest(union3Type);
//        union3Type.toT1();
        union3Type.toT2();
//        union3Type.toT3();
    }
    
    public static void union2TypeTest(Union2Type<String, Demo> union2Type) {
        System.out.println("U2: is string: "  + union2Type.isT1());
        System.out.println("U2: is demo: " + union2Type.isT2());
    }
    
    public static void union3TypeTest(Union3Type<String, Demo, Number> union3Type) {
        System.out.println("U3: is string: " + union3Type.isT1());
        System.out.println("U3: is demo: " + union3Type.isT2());
        System.out.println("U3: is number: " + union3Type.isT3());
    }
    
    public static class Demo {
    }
}
