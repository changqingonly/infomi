package net.infomi.common.starters.selectors;

import net.infomi.common.starters.config.TracerFilterConfig;
import net.infomi.common.starters.filters.TracerFilter;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 登录鉴权选择器
 *
 * @author hongcq
 * @since 2020/07/20
 */
public class TracerSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                TracerFilterConfig.class.getName(),
                TracerFilter.class.getName()
        };
    }

}
