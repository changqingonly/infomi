package net.infomi.common.starters.selectors;

import net.infomi.common.starters.config.SessionWebMvcConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 登录鉴权选择器
 *
 * @author hongcq
 * @since 2020/07/20
 */
public class SessionAuthSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                SessionWebMvcConfig.class.getName()
        };
    }

}
