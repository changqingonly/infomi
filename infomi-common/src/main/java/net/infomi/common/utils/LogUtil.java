package net.infomi.common.utils;

import org.slf4j.MDC;

/**
 * 日志工具类
 *
 * @author hongcq
 * @since 2020/06/21
 */
public class LogUtil {

    /** 日志ID */
    public final static String TRACE_ID = "traceId";

    /**
     * 获取当前线程TreadContext里数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * 设置traceId
     */
    public static void setDefaultLogContext() {
        String traceId = StrUtil.getRandom(8);
        setLogContext(TRACE_ID, traceId);
    }

    /**
     * 移除默认标记列表
     */
    public static void removeDefaultLogContext() {
        removeLogContext(TRACE_ID);
    }

    /**
     * MDC日志开始
     */
    public static void setLogContext(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 移除标记
     */
    public static void removeLogContext(String key) {
        MDC.remove(key);
    }

    /**
     * 移除标记
     *
     * @param keys log context里设置的${key}
     */
    public static void removeLogContext(String ... keys) {
        if (keys.length > 0) {
            for (int i = 0; i < keys.length; i++) {
                removeLogContext(keys[i]);
            }
        }
    }

}
