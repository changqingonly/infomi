package net.infomi.common.consts;

/**
 * 系统常量定义
 *
 * @author hongcq
 * @since 2020/07/18
 */
public class SysConsts {

    /** 请求成功 */
    public static final int SUCCESS = 1;
    /** 进行中 */
    public static final int ING = 2;
    /** 请求失败 */
    public static final int FAIL = 3;
    /** 未知 */
    public static final int UNKNOWN = 4;

    /** starter版本号 */
    public static final String version = "1.0.0";

    public static final String TRACER_PREFIX = "tracer";
    public static final String SESSION_PREFIX = "session";
    public static final String AUTH = "session.auth";

    /**
     * 请求头ID
     */
    public static final String HTTP_HEADER_TRACE_ID = "request_trace_id";
}
