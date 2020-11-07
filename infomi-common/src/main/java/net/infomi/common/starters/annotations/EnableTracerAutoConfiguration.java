package net.infomi.common.starters.annotations;

import net.infomi.common.starters.properties.TracerProperties;
import net.infomi.common.starters.selectors.TracerSelector;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用Passport验证模块
 *
 * @author hongcq
 * @since 2020/07/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties({TracerProperties.class})
@Import(TracerSelector.class)
public @interface EnableTracerAutoConfiguration {

}
