package org.cqq.openlibrary.common.A_configexample;

//@Configuration
//public class MybatisPlusConfig {
//
//    @Bean
//    public MybatisPlusInterceptor paginationInterceptor() {
//        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
//        paginationInnerInterceptor.setOverflow(false);
//        paginationInnerInterceptor.setMaxLimit(-1L);
//        paginationInnerInterceptor.setOptimizeJoin(true);
//        interceptor.addInnerInterceptor(paginationInnerInterceptor); // 如果配置多个插件, 切记分页最后添加
//        return interceptor;
//    }
//}