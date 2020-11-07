package net.infomi.common.dto;

import net.infomi.common.consts.SysConsts;

/**
 * 服务处理类返回对象
 *
 * @param <T>
 * @author hongcq
 * @since 2020/07/19
 */
public class ResultDTO<T> {

    private int code;
    private String msg;
    private T data;

    public ResultDTO(int code) {
        this.code = code;
        this.msg = "";
        this.data = null;
    }

    public ResultDTO(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public ResultDTO(int code, String msg, T data) {
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
    public static ResultDTO instance(int code) {
        return new ResultDTO(code);
    }
    public static ResultDTO instance(int code, String msg) {
        return new ResultDTO(code, msg);
    }
    public static ResultDTO instance(int code, String msg, Object data) {
        return new ResultDTO(code, msg, data);
    }

    /**
     * 实例化一个返回对象
     *
     * @param data
     * @return ResultDTO
     */
    public static ResultDTO instance(Object data) {
        if (data instanceof Integer) {
            return new ResultDTO((Integer) data, "");
        } else if (data instanceof String) {
            return new ResultDTO(SysConsts.SUCCESS, (String) data);
        } else {
            return new ResultDTO(SysConsts.SUCCESS, "success", data);
        }
    }

    @Override
    public String toString() {
        return "ResultDTO{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
