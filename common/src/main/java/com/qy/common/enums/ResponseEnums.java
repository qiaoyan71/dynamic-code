package com.qy.common.enums;
/**
 * 请求返回枚举类
 * @date 2021/04/09 16:28
 **/
public enum ResponseEnums {
    /**
     * 通用正常返回状态
     */
    SUCCESS("0", "成功"),
    /**
     * 通用异常返回状态
     */
    ERROR("-1", "失败"),
    /**
     * 系统异常
     */
    SYS_EXCEPTION("500", "系统异常"),
    /**
     * 通用权限异常返回状态
     */
    NO_AUTH("403", "无权限"),
    /**
     * 请先登录
     */
    SC_UNAUTHORIZED("401", "请先登录"),
    /**
     * 通用参数异常返回状态
     */
    ARGUMENT_EXCEPTION("40000", "参数有误"),
    ;

    /**
     * 通用返回值异常
     */
    private static final String COMMON_RETURN_PARAM_ERROR_CODE = "10000";
    /**
     * 通用参数不完整错误码
     */
    private static final String COMMON_UNCOMPLETION_PARAMS_CODE = "100010";
    /**
     * 通用参数校验错误码
     */
    private static final String COMMON_UN_VALID_PARAMS_CODE = "100002";
    /**
     * 审核相关错误业务码
     */
    private static final String AUDIT_BUSSINESS_CODE = "201000001";

    /**
     * 返回码
     */
    private String retCode;

    /**
     * 返回消息
     */
    private String message;

    ResponseEnums(String retCode, String message) {
        this.retCode = retCode;
        this.message = message;
    }

    public String getRetCode() {
        return retCode;
    }

    public String getMessage() {
        return message;
    }

}
