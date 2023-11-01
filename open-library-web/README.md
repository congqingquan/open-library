需要手动注入的 component:

1. GlobalExceptionHandler: 需要通过继承的方式使用，子类上必须需标注：@ControllerAdvice
2. GlobalFilter: WebMvcConfigurer 中配置