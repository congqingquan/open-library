package org.cqq.openlibrary.common.A_configexample;

//@EnableOpenApi
//@EnableKnife4j
//@EnableSwagger2
//@Import(BeanValidatorPluginsConfiguration.class)
//@Configuration
//public class Swagger2Config {
//
//    /**
//     * Swagger
//     *          3.0.x 访问地址：http://ip:port/{context-path}/swagger-ui/index.html
//     *          2.9.x 访问地址：http://ip:port/{context-path}/swagger-ui.html
//     *
//     * Knife4j 访问地址：http://ip:port/{context-path}/doc.html
//     * Knife4j 点击空白页：@Api 注解配置中不要出现符号 '/' 即可解决
//     */
//
//    @Value("${swagger.enable}")
//    private boolean enable;
//
//    @Value("${swagger.title}")
//    private String title;
//
//    @Value("${swagger.description}")
//    private String description;
//
//    @Value("${swagger.version}")
//    private String version;
//
//    /**
//     * 创建API
//     */
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30)
//            .apiInfo(apiInfo())
//            // 解决 LocalTime 的文档展示格式
//            .directModelSubstitute(LocalTime.class, String.class)
//            .select()
//            // 需要暴露的API
//            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//            .paths(PathSelectors.any())
//            .build()
//            // 是否启用API文档
//            .enable(enable);
//    }
//
//    /**
//     * 创建基本信息
//     * @return
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//            // 标题
//            .title(title)
//            // 描述
//            .description(description)
//            // 联系人
//            //.contact(new Contact("", "", ""))
//            // 版本
//            .version(version)
//            .build();
//    }
//}