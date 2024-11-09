package okhttp;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * OkHttp utils 测试接口
 *
 * @author Qingquan
 */
@RestController
public class OkHttpUtilsTestController {
    
    @GetMapping("/okhttp/get")
    public String get(HttpServletRequest request) {
        
        printlnRequestLine(request);
        printlnHeaders(request);
        printlnParameters(request);
        printlnRequestBody(request);
        printlnMultipartFile(request);
        
        return "GET REQUEST SUCCESS";
    }
    
    @PostMapping("/okhttp/postJSON")
    public String postJSON(HttpServletRequest request) {
        
        printlnRequestLine(request);
        printlnHeaders(request);
        printlnParameters(request);
        printlnRequestBody(request);
        printlnMultipartFile(request);
        
        return "POST JSON REQUEST SUCCESS";
    }
    
    @PostMapping("/okhttp/postForm")
    public String postFormData(HttpServletRequest request) {
        
        printlnRequestLine(request);
        printlnHeaders(request);
        printlnParameters(request);
        printlnRequestBody(request);
        printlnMultipartFile(request);
        
        return "POST FROM REQUEST SUCCESS";
    }
    
    @PostMapping("/okhttp/postMultipartForm")
    public void postMultipartForm(MultipartHttpServletRequest request) {
        
        printlnRequestLine(request);
        printlnHeaders(request);
        printlnParameters(request);
        printlnRequestBody(request);
        printlnMultipartFile(request);
        
    }
    
    // ========================================== Print ==========================================
    
    public void printlnRequestLine(HttpServletRequest request) {
        System.out.println("========================= RequestLine =========================");
        String method = request.getMethod(); // 获取请求方式
        String requestURI = request.getRequestURI(); // 获取请求路径
        String protocol = request.getProtocol(); // 获取请求协议
        System.out.println(method + " " + requestURI + " " + protocol);
    }
    
    public void printlnParameters(HttpServletRequest request) {
        System.out.println("========================= Parameters =========================");
        request.getParameterMap().forEach((key, value) -> System.out.println(key + ":" + String.join("/", value)));
    }
    
    public void printlnHeaders(HttpServletRequest request) {
        System.out.println("========================= Headers =========================");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String headerVal = request.getHeader(header);
            System.out.println(header + ": " + headerVal);
        }
    }
    
    public void printlnRequestBody(HttpServletRequest request) {
        System.out.println("========================= Request body =========================");
        try {
            ServletInputStream inputStream = request.getInputStream();
            int len;
            for (byte[] buff = new byte[1024]; (len = inputStream.read(buff)) != -1; ) {
                System.out.print(new String(buff, 0, len, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }
    
    public void printlnMultipartFile(HttpServletRequest request) {
        System.out.println("========================= Multipart file =========================");
        if (request instanceof MultipartHttpServletRequest multipartHttpServletRequest) {
            MultiValueMap<String, MultipartFile> multiFileMap = multipartHttpServletRequest.getMultiFileMap();
            multiFileMap.forEach((key, value) -> {
                for (MultipartFile multipartFile : value) {
                    System.out.println(multipartFile.getName());
                    System.out.println(multipartFile.getOriginalFilename());
                }
            });
        }
    }
}