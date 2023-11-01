需要手动注入的 component:

1. SpringTransactionUtils: @Configuration 中配置，核心是注入 `PlatformTransactionManager`。