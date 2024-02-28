package org.cqq.oplibrary.web.config_example;

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
//@Order(1)
//@Configuration
//public class ApplicationJacksonConfig {
//
//    // 直接配置 ObjectMapper
//    // @Bean
//    public ObjectMapper objectMapper() {
//        return new JacksonConfig().buildObjectMapper();
//    }
//
//    // 根据 SpringBoot 提供的配置类进行配置
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer springBootJacksonConfig() {
//        return new priv.cqq.common.config.SpringBootJacksonConfig();
//    }
//}