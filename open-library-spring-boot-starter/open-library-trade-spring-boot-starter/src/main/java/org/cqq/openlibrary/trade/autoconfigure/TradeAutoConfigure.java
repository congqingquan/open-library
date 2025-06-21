package org.cqq.openlibrary.trade.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Trade auto configure
 *
 * @author Qingquan
 */
@Slf4j
@EnableConfigurationProperties(TradeConfig.class)
@Configuration
public class TradeAutoConfigure {
}
