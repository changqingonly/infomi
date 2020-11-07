package net.infomi.common.starters.annotations;

import java.lang.annotation.*;

/**
 * 免鉴权注解
 *
 * @author hongcq
 * @since 2020/07/18
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NotSessionAuth {

}
