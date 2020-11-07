package net.infomi.common.starters.config;

import net.infomi.common.services.SessionService;
import net.infomi.common.starters.interceptors.SessionInterceptor;
import net.infomi.common.starters.properties.SessionProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * 登录鉴权拦截器注册类
 */
@AutoConfigureAfter(WebMvcConfigurationSupport.class)
public class SessionWebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private SessionProperties properties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor(new SessionService(), properties))
                .addPathPatterns("/**")
                .excludePathPatterns(new String[] {"/static/**", "/uploader/**"})
                .order(99);
        super.addInterceptors(registry);
    }

}