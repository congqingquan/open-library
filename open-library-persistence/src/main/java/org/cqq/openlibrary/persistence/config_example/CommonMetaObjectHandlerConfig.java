//@Configuration
//public class CommonMetaObjectHandlerConfig {
//
//    /**
//     * 解决并发环境下线程在 updateTimeSupplier 中获取的创建时间与在 createTimeSupplier 中设置的时间戳不一致问题
//     */
//    private static final ThreadLocal<LocalDateTime> createTimeHolder = new ThreadLocal<>();
//
//    private static final Supplier<Object> createTimeSupplier = () -> {
//        LocalDateTime now = LocalDateTime.now();
//        createTimeHolder.set(now);
//        return now;
//    };
//
//    private static final Supplier<Object> updateTimeSupplier = () -> {
//        LocalDateTime createTime = createTimeHolder.get();
//        createTimeHolder.remove();
//        return createTime;
//    };
//
//    /**
//     * Mybatis plus 字段注入拦截器
//     */
//    @Bean
//    public CommonMetaObjectHandler commonMetaObjectHandler() {
//        List<FieldFillConfig> insertFieldConfig = new ArrayList<>();
//        insertFieldConfig.add(
//                new FieldFillConfig(
//                        TableBaseFieldEnum.CREATE_USER.getBeanFieldName(),
//                        JwsUserUtils::currentUserId,
//                        FieldFillConfig.createIsNullPredicate(TableBaseFieldEnum.CREATE_USER.getBeanFieldName())
//                )
//        );
//        insertFieldConfig.add(
//                new FieldFillConfig(
//                        TableBaseFieldEnum.CREATE_TIME.getBeanFieldName(),
//                        createTimeSupplier,
//                        FieldFillConfig.createIsNullPredicate(TableBaseFieldEnum.CREATE_TIME.getBeanFieldName())
//                )
//        );
//        insertFieldConfig.add(
//                new FieldFillConfig(
//                        TableBaseFieldEnum.UPDATE_USER.getBeanFieldName(),
//                        JwsUserUtils::currentUserId,
//                        FieldFillConfig.createIsNullPredicate(TableBaseFieldEnum.UPDATE_USER.getBeanFieldName())
//                )
//        );
//        insertFieldConfig.add(
//                new FieldFillConfig(
//                        TableBaseFieldEnum.UPDATE_TIME.getBeanFieldName(),
//                        updateTimeSupplier,
//                        FieldFillConfig.createIsNullPredicate(TableBaseFieldEnum.UPDATE_TIME.getBeanFieldName())
//                )
//        );
//
//        List<FieldFillConfig> updateFieldConfig = new ArrayList<>();
//        updateFieldConfig.add(new FieldFillConfig(TableBaseFieldEnum.UPDATE_USER.getBeanFieldName(), JwsUserUtils::currentUserId, (metaObjectHandler, metaObject) -> true));
//        updateFieldConfig.add(new FieldFillConfig(TableBaseFieldEnum.UPDATE_TIME.getBeanFieldName(), LocalDateTime::now, (metaObjectHandler, metaObject) -> true));
//        return new CommonMetaObjectHandler(insertFieldConfig, updateFieldConfig);
//    }
//
//    /**
//     * 分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//        paginationInterceptor.setOverflow(false);
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        paginationInterceptor.setLimit(-1);
//        // 开启 count 的 join 优化,只针对部分 left join
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }
//}