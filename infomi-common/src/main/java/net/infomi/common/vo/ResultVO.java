package net.infomi.common.vo;

import net.infomi.common.consts.SysConsts;

/**
 * 服务处理类返回对象
 *
 * @param <T>
 * @author hongcq
 * @since 2020/07/19
 */
public class ResultVO<T> {

    private int code;
    private String msg;
    private T data;

    public ResultVO(int code) {
        this.code = code;
        this.msg = "";
        this.data = null;
    }

    public ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 初始化一个返回对象
     *
     * @param code
     * @return ResultDTO
     */
    public static ResultVO instance(int code) {
        return new ResultVO(code);
    }
    public static ResultVO instance(int code, String msg) {
        return new ResultVO(code, msg);
    }
    public static ResultVO instance(int code, String msg, Object data) {
        return new ResultVO(code, msg, data);
    }

    /**
     * 实例化一个返回对象
     *
     * @param data
     * @return ResultDTO
     */
    public static ResultVO instance(Object data) {
        if (data instanceof Integer) {
            return new ResultVO((Integer) data, "");
        } else if (data instanceof String) {
            return new ResultVO(SysConsts.SUCCESS, (String) data);
        } else {
            return new ResultVO(SysConsts.SUCCESS, "success", data);
        }
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
