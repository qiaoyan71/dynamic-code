package com.qy.common.dto;

import com.qy.common.enums.ResponseEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回结果处理类
 *
 * @author qinhuiwei
 * @date 2021/04/09 16:24
 **/
@Data
@ApiModel(value = "ReturnResult", description = "返回结果处理类")
public class ReturnResult<T> implements Serializable {

    private static final long serialVersionUID = 5446374548003371662L;
    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private transient T data;

    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码")
    private String retCode;

    /**
     * 消息提示
     */
    @ApiModelProperty(value = "消息提示")
    private String message;

    /**
     * 扩展返回码
     */
    @ApiModelProperty(value = "扩展返回码")
    private String extraCode;

    /**
     * 扩展返回提示消息
     */
    @ApiModelProperty(value = "扩展返回提示消息")
    private String extraMessage;

    /**
     * 跟踪堆栈信息
     */
    @ApiModelProperty(value = "跟踪堆栈信息")
    private String tracestack;

    public static <T> ReturnResult<T> getReturnResult() {
        return new ReturnResult<>();
    }

    public ReturnResult<T> setBaseInfo(String retCode, String message) {
        this.retCode = retCode;
        this.message = message;
        return this;
    }

    public ReturnResult<T> setBaseInfo(T data, String retCode, String message) {
        this.data = data;
        this.retCode = retCode;
        this.message = message;
        return this;
    }

    public ReturnResult(String retCode, String msg) {
        this.retCode = retCode;
        this.message = msg;
    }

    private ReturnResult() {
        this.retCode = ResponseEnums.ERROR.getRetCode();
        this.message = ResponseEnums.ERROR.getMessage();
    }

    public ReturnResult(ResponseEnums rspEnum) {
        this(rspEnum.getRetCode(), rspEnum.getMessage());
    }

    public static <T> ReturnResult<T> success(T data) {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(data, ResponseEnums.SUCCESS.getRetCode(), ResponseEnums.SUCCESS.getMessage());
    }

    public static <T> ReturnResult<T> success() {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(ResponseEnums.SUCCESS.getRetCode(), ResponseEnums.SUCCESS.getMessage());
    }

    public static <T> ReturnResult<T> error(ResponseEnums rspEnum) {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(rspEnum.getRetCode(), rspEnum.getMessage());
    }

    public static <T> ReturnResult<T> error(T data, ResponseEnums rspEnum) {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(data, rspEnum.getRetCode(), rspEnum.getMessage());
    }

    public static <T> ReturnResult<T> error(String code, String msg) {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(code, msg);
    }

    public static <T> ReturnResult<T> error(String code, String msg, T data) {
        ReturnResult<T> returnResult = ReturnResult.getReturnResult();
        return returnResult.setBaseInfo(data, code, msg);
    }

    public boolean isSuccess() {
        return this.retCode.equals(ResponseEnums.SUCCESS.getRetCode());
    }

    public boolean isFail() {
        return !isSuccess();
    }

    public boolean hasData() {
        return (this.retCode.equals(ResponseEnums.SUCCESS.getRetCode()) && !Objects.isNull(data));
    }

    public boolean hasNoData() {
        return !hasData();
    }

}
