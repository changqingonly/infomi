package net.infomi.common.starters.interceptors;

import net.infomi.common.consts.SysConsts;
import net.infomi.common.dto.ResultDTO;
import net.infomi.common.dto.SessionDTO;
import net.infomi.common.exceptions.SessionException;
import net.infomi.common.services.SessionService;
import net.infomi.common.starters.annotations.NotSessionAuth;
import net.infomi.common.starters.properties.SessionProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录验证请求拦截器
 *
 * @author hongcq
 * @since 2020/07/13
 */
public class SessionInterceptor implements HandlerInterceptor {

    private static Logger logger = LogManager.getLogger(SessionInterceptor.class);

    /** 用户服务操作类 */
    private SessionService sessionService;

    /** 属性配置信息 */
    private SessionProperties properties;

    @Value("${spring.application.name:app}")
    private String appId;

    public SessionInterceptor(SessionService service, SessionProperties properties) {
        this.sessionService = service;
        this.properties = properties;
    }

    /**
     * 进入controller层之前拦截请求
     *
     * @param request
     * @param response
     * @param handler
     * @return true/false
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!properties.isEnabled()) {
            return true;
        // 带有NotLoginAuth注解的controller不需要验证登录态
        } else if (doNotAuth(handler, request)) {
            logger.info("msg[do not check login session]");
            return true;
        } else if (isExcludePath(request)) {
            logger.info("msg[exclude auth-uri, do not check session]");
            return true;
        }

        // 获取登录属性
        String uid = (String) request.getAttribute("uid");
        String accessToken = (String) request.getAttribute("accessToken");
        ResultDTO result = sessionService.checkLogin(new SessionDTO(uid, accessToken, appId));
        if (result == null || result.getCode() != SysConsts.SUCCESS) {
            logger.warn("msg[login fail] code[{}] errMsg[{}]", result.getCode(), result.getMsg());
            throw new SessionException(result.getCode(), result.getMsg());
        }

        return true;
    }

    /**
     * 判断是否排除在外的URL
     *
     * @param request
     * @return boolean true:不需要验证登录态,false:需要验证登录态
     */
    private boolean isExcludePath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        List<String> list = properties.getExcludeList();
        if (list == null || list.size() < 1) {
            return false;
        }
        for (String item : list) {
            // 仅仅支持前缀匹配
            if (item.contains("*")) {
                item = item.replaceAll("\\*", "");
                if (uri.startsWith(item)) {
                    return true;
                }
            } else if (uri.equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 接口权限判断
     *
     * @param handler
     * @param request
     * @return boolean true:不需要验证登录态,false:需要验证登录态
     */
    private boolean doNotAuth(Object handler, HttpServletRequest request) {
        NotSessionAuth notAuth = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取接口上的注解，如果有注解则放行，没有注解则进行验证
            notAuth = handlerMethod.getMethod().getAnnotation(NotSessionAuth.class);
        }
        // 默认全部请求都要验证
        return notAuth != null;
    }

    /**
     * 处理请求完成后视图渲染之前的处理操作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        logger.info("SessionInterceptor.postHandle-handler: " + handler);
        if (modelAndView != null) {
            logger.info("SessionInterceptor.postHandle-modelAndView: " + modelAndView.getClass().toString());
        }
    }

    /**
     * 视图渲染之后的操作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        logger.info("SessionInterceptor.afterCompletion()");
    }
}
