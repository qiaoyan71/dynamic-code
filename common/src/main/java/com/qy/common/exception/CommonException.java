package com.qy.common.exception;

import com.qy.common.enums.ResponseEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一异常处理类
 *
 * @author qinhuiwei
 * @date 2021/04/23 16:49
 **/
@Getter
@Setter
@AllArgsConstructor
public class CommonException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -6627493064264411506L;

    private static final String DEFAULT_MSG = "系统异常，请稍后重试";

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;

    public CommonException(ResponseEnums responseEnums) {
        super(responseEnums.getMessage());
        this.code = responseEnums.getRetCode();
        this.msg = responseEnums.getMessage();
    }

    public CommonException(ResponseEnums responseEnums, String meesage) {
        super(meesage);
        this.code = responseEnums.getRetCode();
        this.msg = meesage;
    }

    public CommonException() {
        super(DEFAULT_MSG);
        this.msg = DEFAULT_MSG;
        this.code = ResponseEnums.SYS_EXCEPTION.getRetCode();
    }

    public CommonException(String message) {
        super(message);
        this.msg = message;
        this.code = ResponseEnums.SYS_EXCEPTION.getRetCode();
    }

    public CommonException(Throwable cause) {
        super(cause);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.msg = message;
        this.code = ResponseEnums.SYS_EXCEPTION.getRetCode();
    }

    public CommonException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = message;
        this.code = ResponseEnums.SYS_EXCEPTION.getRetCode();
    }

}
