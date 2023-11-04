//@Configuration
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//public class WebApplicationConfig implements WebMvcConfigurer {
//
//    private final List<HandlerInterceptor> handlerInterceptors;
//
//    public WebApplicationConfig(List<HandlerInterceptor> handlerInterceptors) {
//        this.handlerInterceptors = handlerInterceptors;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
//            registry.addInterceptor(handlerInterceptor)
//                    .addPathPatterns("/**")
//                    .excludePathPatterns(
//                            "/authenticate/**",
//                            "/error", "/swagger-resources/**", "/v3/api-docs", "/favicon.ico", "/doc.html", "/webjars/**",
//                            "/swagger-ui/**"
//                    );
//        }
//    }
//
//    @Bean
//    public FilterRegistrationBean<CommonFilter> globalFilterFilter() {
//        FilterRegistrationBean<CommonFilter> filterBean = new FilterRegistrationBean<>();
//        filterBean.setFilter(new CommonFilter());
//        filterBean.setUrlPatterns(Collections.singletonList("/*"));
//        return filterBean;
//    }
//}