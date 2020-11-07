package net.infomi.common.starters.properties;

import net.infomi.common.consts.SysConsts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 属性配置类
 *
 * @author hongcq
 * @since 2020/07/18
 */
@ConfigurationProperties(prefix = SysConsts.TRACER_PREFIX)
public class TracerProperties {

    @Value("${tracer.enabled:true}")
    private boolean enabled;

    @Value("${tracer.request:true}")
    private boolean traceRequest;

    @Value("${tracer.response:true}")
    private boolean traceResponse;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean setTraceRequest(boolean val) {
        return this.traceRequest = val;
    }

    public boolean setTraceResponse(boolean val) {
        return this.traceResponse = val;
    }

    public boolean getTraceRequest() {
        return this.traceRequest;
    }

    public boolean getTraceResponse() {
        return this.traceResponse;
    }
}
