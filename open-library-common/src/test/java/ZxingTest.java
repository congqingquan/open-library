import com.google.zxing.Result;
import org.cqq.openlibrary.common.util.ZxingUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ZxingTest {
    
    @Test
    public void qrcodeTest() throws Exception {
        Path qrcodeFilePath = Path.of("C:/Users/Administrator/Desktop/qrcode.png");
        
        ByteArrayOutputStream qrcodeOutputStream = ZxingUtils.drawQRCode(
                "17600073620",
                500,
                500,
                "png",
                new FileInputStream("C:\\Users\\Administrator\\Desktop\\logo.png"),
                25
        );
        Files.write(qrcodeFilePath, qrcodeOutputStream.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        
        ByteArrayInputStream qrcodeInputStream = new ByteArrayInputStream(Files.readAllBytes(qrcodeFilePath));
        Result decode = ZxingUtils.decode(qrcodeInputStream);
        System.out.println("Text: " + decode.getText());
    }
}
