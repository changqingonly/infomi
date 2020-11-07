package net.infomi.common.starters;

import net.infomi.common.consts.SysConsts;
import net.infomi.common.services.SessionService;
import net.infomi.common.starters.properties.SessionProperties;
import net.infomi.common.starters.selectors.SessionAuthSelector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Session验证模块自动装载
 *
 * Note：按如下步骤启用该类
 * 1) 需要在META-INF/spring.factories中设置：
 * org.springframework.boot.autoconfigure.EnableAutoConfiguration=net.infomi.common.starters.SessionStarterAutoConfiguration
 * 2) 把下面注解加上：
 *   - @Configuration
 *   - @EnableConfigurationProperties({SessionProperties.class})
 *   - @Import(SessionAuthSelector.class)
 * @author hongcq
 * @since 2020/07/19
 */
//@Configuration
//@EnableConfigurationProperties({SessionProperties.class})
//@Import(SessionAuthSelector.class)
public class SessionStarterAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = SysConsts.AUTH, value = "enabled", havingValue = "true")
    public SessionService getPassportService() {
        return new SessionService();
    }

}
