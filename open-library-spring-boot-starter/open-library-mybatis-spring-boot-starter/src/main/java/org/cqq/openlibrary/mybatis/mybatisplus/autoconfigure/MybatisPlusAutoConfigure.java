package org.cqq.openlibrary.mybatis.mybatisplus.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.mybatis.mybatisplus.tenant.TenantHandler;
import org.cqq.openlibrary.spring.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Mybatis-plus auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(MybatisPlusConfig.class)
@Configuration
public class MybatisPlusAutoConfigure {
    
    /**
     * 多租户插件
     */
    @ConditionalOnProperties(properties = {
            @ConditionalOnProperties.Property(name = "open-library.mybatis.mybatis-plus.tenant-config.tenant-id-column")
    })
    @Bean
    public MybatisPlusInterceptor tenantInterceptor(MybatisPlusConfig mybatisPlusConfig) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        MybatisPlusConfig.TenantConfig tenantConfig = mybatisPlusConfig.getTenantConfig();
        interceptor.addInnerInterceptor(
                new TenantLineInnerInterceptor(new TenantHandler(tenantConfig.getIgnoreTables(), tenantConfig.getTenantIdColumn()))
        );
        return interceptor;
    }
    
    /**
     * 分页插件
     */
    @Bean
    @Order
    public MybatisPlusInterceptor pageInterceptor(MybatisPlusConfig mybatisPlusConfig) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件 (如果配置多个插件, 切记分页最后添加)
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(false);
        // 高版本需要配置为 null 才表示对不限制最大条数，注意！
        paginationInnerInterceptor.setMaxLimit(null);
        paginationInnerInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
    
}
