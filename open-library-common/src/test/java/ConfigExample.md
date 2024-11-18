## 代码封装总结
1. 实例：
    1. 纯实例：尽量不依赖内部静态，皆为成员性质的（如：org.cqq.openlibrary.common.component.template.* 等）
2. 静态：
    1. 纯静态：不依赖任何的内部非静态。如：
       - org.cqq.openlibrary.common.util.CollectionUtils
       - org.cqq.openlibrary.common.util.ArrayUtils
       - org.cqq.openlibrary.common.util.OkHttpUtils
       - org.cqq.openlibrary.common.util.JSONUtils
    2. 依赖非静态：
       1. 非静态设置为函数参数。如：
          - org.cqq.openlibrary.common.util.wechat.WechatApiUtils
          - org.cqq.openlibrary.common.util.CodeUtils
       2. 应用启动时，手动进行非静态的依赖注入。如：
          - org.cqq.openlibrary.common.jwt.JWSUserUtils

## OpenLibraryConfig (考虑做成 starter)

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class OpenLibraryConfig implements ApplicationRunner {
    
    @Bean
    public JWSAuthConfig jwsAuthConfig() {
        return new JWSAuthConfig();
    }
    
    @Bean
    public WechatCertConfig wechatCertConfig() {
        return new WechatCertConfig();
    }
    
    @Bean
    public WechatMiniProgramConfig wechatMiniProgramConfig() {
        return new WechatMiniProgramConfig();
    }
    
    @Bean
    public AlipayCertConfig alipayCertConfig() {
        return new AlipayCertConfig();
    }
    
    @Bean
    public AliYunSMSConfig aliYunSMSConfig() {
        return new AliYunSMSConfig();
    }
    
    @Override
    public void run(ApplicationArguments args) {
        // 系统初始化
        SystemInitializer.init();
        
        // JWS 鉴权配置
        JWSUserUtils.init(jwsAuthConfig());
        
        // 微信证书缓存
        SimpleCache.put("DEFAULT_WECHAT_PAYMENT_CERT_CONFIG", WechatConverter.certConfig2RSAAutoCertificateConfig(wechatCertConfig()));
        
        // 支付宝 API Client 缓存
        SimpleCache.put("DEFAULT_ALIPAY_API_CLIENT", AlipayConverter.certConfig2ApiClient(alipayCertConfig()));
        
        // 阿里云 SMS Client 缓存
        AliYunSMSConfig aliYunSMSConfig = aliYunSMSConfig();
        SimpleCache.put("DEFAULT_ALI_YUN_SMS_CLIENT", AliYunConverter.smsConfig2Client(aliYunSMSConfig));
        
        log.info("OpenLibrary 应用启动后置处理完成");
    }
}
```

## ApplicationJacksonConfig

```java
/**
 * Application JSON 全局配置 (配置 Spring 使用的 ObjectMapper)
 *
 * 1. 可以应用在接口响应时的结果序列化: 比如转换接口响应实体中日期字段的格式
 * 2. 通过注入的 ObjectMapper 进行 JSON 序列化 & 反序列化
 *
 * 注意：
 * 1. 下面的配置无法应用到接口入参实体的反序列化。比如处理入参实体的日期字段，仍需要通过 @DateTimeFormat 注解。
 * 2. 声明式的在字段上添加的 @JsonSerialize(using = XXXSerializer.class) @JsonDeserialize(using = XXXDeserializer.class) 的优先级要高于配置中配置的序列化器
 */
@Order(1)
@Configuration
public class ApplicationJacksonConfig {

    // 直接配置 ObjectMapper
    // @Bean
    public ObjectMapper objectMapper() {
        return new JacksonConfig().buildObjectMapper();
    }

    // 根据 SpringBoot 提供的配置类进行配置
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer springBootJacksonConfig() {
        return new SpringBootJacksonConfig();
    }
}
```

## CommonExceptionHandler

```java
@RestControllerAdvice
public class WebServerExceptionHandler extends CommonExceptionHandler {
}
```

## CommonFilter

```java
    @Bean
    public FilterRegistrationBean<CommonFilter> globalFilterFilter() {
        FilterRegistrationBean<CommonFilter> filterBean = new FilterRegistrationBean<>();
        filterBean.setName("CommonFilter");
        filterBean.setFilter(new CommonFilter());
        filterBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterBean;
    }
```

## WebLogAspect

```java
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class WebLogAspect extends CommonLogAspect {
    
    @Pointcut("execution(* com.smkj..*.*Controller.*(..))")
    private void webLog() {
    }
    
    @Around("webLog()")
    public Object webLogAround(ProceedingJoinPoint point) throws Throwable {
        return super.webLogAround(point);
    }
}
```

## Swagger2Config

目前一般直接使用 knife4j 替代，不用配置 swagger-ui

```java
@EnableOpenApi
@EnableKnife4j
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
public class Swagger2Config {

    /**
     * Swagger
     *          3.0.x 访问地址：http://ip:port/{context-path}/swagger-ui/index.html
     *          2.9.x 访问地址：http://ip:port/{context-path}/swagger-ui.html
     *
     * Knife4j 访问地址：http://ip:port/{context-path}/doc.html
     * Knife4j 点击空白页：@Api 注解配置中不要出现符号 '/' 即可解决
     */

    @Value("${swagger.enable}")
    private boolean enable;

    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.description}")
    private String description;

    @Value("${swagger.version}")
    private String version;

    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            // 解决 LocalTime 的文档展示格式
            .directModelSubstitute(LocalTime.class, String.class)
            .select()
            // 需要暴露的API
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build()
            // 是否启用API文档
            .enable(enable);
    }

    /**
     * 创建基本信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            // 标题
            .title(title)
            // 描述
            .description(description)
            // 联系人
            //.contact(new Contact("", "", ""))
            // 版本
            .version(version)
            .build();
    }
}
```

## MybatisPlus

```java
@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(false);
        // 高版本需要配置为 null 才表示对不限制最大条数，注意！
        paginationInnerInterceptor.setMaxLimit(null);
        paginationInnerInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor); // 如果配置多个插件, 切记分页最后添加
        return interceptor;
    }
}
```

## MybatisPlusTypeHandlerConfig

application.yaml

```yaml
mybatis-plus:
  configuration:
    default-enum-type-handler: org.cqq.openlibrary.common.presistent.mybatis.typehandler.EnumTypeHandler
  type-handlers-package: org.cqq.openlibrary.common.presistent.mybatis.typehandler
```

## lombok.config

```properties
config.stopBubbling = true
lombok.tostring.callsuper=CALL
lombok.equalsandhashcode.callsuper=CALL
lombok.accessors.chain=true
```
