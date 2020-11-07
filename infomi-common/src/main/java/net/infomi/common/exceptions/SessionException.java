package net.infomi.common.exceptions;

import net.infomi.common.consts.SysConsts;

/**
 * 会话异常
 *
 * @auther hongcq
 * @Date: 2020/06/21
 */
public class SessionException extends RuntimeException {

    private int code;

    public SessionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SessionException(String message) {
        super(message);
        this.code = SysConsts.FAIL;
    }

    public int getCode() {
        return this.code;
    }
}
