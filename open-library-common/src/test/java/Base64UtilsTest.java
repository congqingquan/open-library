import org.cqq.openlibrary.common.util.Base64Utils;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Base64UtilsTest {

    @Test
    public void setTest() throws Exception {
        String filePath = "C:\\Users\\EDY\\Desktop\\身份证人像.jpg";
        URL file = new URL("file", null, filePath);
        String encoded = new String(Base64Utils.encode(file), StandardCharsets.UTF_8);
        
        byte[] decode = Base64Utils.decode(encoded.getBytes(StandardCharsets.UTF_8));
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\EDY\\Desktop\\身份证人像Copy.jpg")) {
            fileOutputStream.write(decode);
        }
    }
}