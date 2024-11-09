package okhttp;

import io.vavr.Tuple3;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.cqq.openlibrary.common.util.OkHttpUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class OkHttpUtilsTest {

    @Test
    public void get() throws Exception {
        Response response = OkHttpUtils.get(
                "http://localhost:9090/api/okhttp/get",
                Map.of("Token", "CQQ25"),
                Map.of("name", "CQQ", "age", "25")
        );
        System.out.println(response);
        System.out.println(response.body().string());
    }
    
    @Test
    public void postJSON() throws Exception {
        Response response = OkHttpUtils.postJSON(
                "http://localhost:9090/api/okhttp/postJSON",
                Map.of("Token", "CQQ25"),
                Map.of("name", "CQQ", "age", "25")
        );
        System.out.println(response);
        System.out.println(response.body().string());
    }
    
    @Test
    public void postForm() throws Exception {
        Response response = OkHttpUtils.postForm(
                "http://localhost:9090/api/okhttp/postForm",
                Map.of("Token", "CQQ25"),
                Map.of("name", "CQQ", "age", "25")
        );
        System.out.println(response);
        System.out.println(response.body().string());
    }
    
    @Test
    public void postMultipartForm() throws Exception {
        
        File file1 = new File("E:\\dev\\workspace\\idea\\backend\\personal\\open-library\\open-library-common\\src\\test\\java\\okhttp\\file\\File1.txt");
        File file2 = new File("E:\\dev\\workspace\\idea\\backend\\personal\\open-library\\open-library-common\\src\\test\\java\\okhttp\\file\\File2.png");
        File file3 = new File("E:\\dev\\workspace\\idea\\backend\\personal\\open-library\\open-library-common\\src\\test\\java\\okhttp\\file\\File3.txt");
        
        Response response = OkHttpUtils.postMultipartForm(
                "http://localhost:9090/api/okhttp/postMultipartForm",
                Map.of("Token", "CQQ25"),
                Map.of("name", "CQQ", "age", "25"),
                List.of(
                        // 表单字段 uploadFile1: [file1, file2]
                        new Tuple3<>("uploadFile1&2", file1.getName(), RequestBody.create(file1, null)),
                        new Tuple3<>("uploadFile1&2", file2.getName(), RequestBody.create(file2, MediaType.parse("image/png"))),
                        // 表单字段 uploadFile2: [file3]
                        new Tuple3<>("uploadFile3", file3.getName(), RequestBody.create(file3, null))
                )
        );
        System.out.println(response);
        System.out.println(response.body().string());
    }
    
    @Test
    public void error() throws Exception {
        Response response = OkHttpUtils.get(
                "http://localhost:9090/api/okhttp/get2",
                Map.of("Token", "CQQ25"),
                Map.of("name", "CQQ", "age", "25")
        );
        System.out.println(response);
        // ""
        System.out.println(response.message());
        // {"timestamp":"2024-11-08T18:27:10.844+00:00","status":404,"error":"Not Found","path":"/api/okhttp/get2"}
        System.out.println(response.body().string());
    }
}