import org.junit.jupiter.api.Test;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author Qingquan
 */
public class PathPatternTest {
    
    @Test
    public void pathTest() {
        // 1. PathPatternParser 支持尾部 **。不支持非尾部的 **，parse期间会抛出异常 / AntPathMatcher 支持中间部分的 **
        // 2. /{*pathVariable} 支持提取 url 中的参数

//        PathPattern parse = PathPatternParser.defaultInstance.parse("/admin/**/sysUser");
//        PathContainer pathContainer = PathContainer.parsePath("/admin/sysUser/a/b/c", PathContainer.Options.HTTP_PATH);
//        System.out.println(parse.matches(pathContainer));
        
        PathPattern pattern = PathPatternParser.defaultInstance.parse("/admin/sysUser/{*pathVariable}");
        PathContainer pathContainer = PathContainer.parsePath("/admin/sysUser/a/b/c", PathContainer.Options.HTTP_PATH);
        System.out.println(pattern.matches(pathContainer));
        
        PathPattern.PathMatchInfo pathMatchInfo = pattern.matchAndExtract(pathContainer);
        System.out.println(pathMatchInfo);
    }
}
