package net.infomi.common.starters.properties;

import net.infomi.common.consts.SysConsts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 属性配置类
 *
 * @author hongcq
 * @since 2020/07/18
 */
@ConfigurationProperties(prefix = SysConsts.SESSION_PREFIX)
public class SessionProperties {

    @Value("${session.auth.enabled:true}")
    private boolean enabled;

    @Value("#{'${session.auth.exclude-list}'.split(',')}")
    private List<String> excludeList;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getExcludeList() {
        return excludeList;
    }

    public void setExcludeList(List<String> excludeList) {
        this.excludeList = excludeList;
    }
}
