package net.infomi.common.starters.config;

import net.infomi.common.starters.filters.TracerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * 登录鉴权拦截器注册类
 */
public class TracerFilterConfig {

    @Resource
    private TracerFilter tracerFilter;

    /**
     * 日志输入输出打印过滤器
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean logFilter() {
        final FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(this.tracerFilter);
        bean.setOrder(-9999);
        bean.setName("tracerFilter");
        bean.addUrlPatterns("/*");
        return bean;
    }

}