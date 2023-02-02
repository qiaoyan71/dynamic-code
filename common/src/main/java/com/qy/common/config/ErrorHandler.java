package com.qy.common.config;

import com.qy.common.dto.ReturnResult;
import com.qy.common.enums.ResponseEnums;
import com.qy.common.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;

/**
 * 异常信息统一拦截处理
 *
 * @author qiaoyan
 * @since 2021-01-07
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    @Resource
    private Environment env;
    @Value("${platform.error.tracestack-env-range:uat,sit,pre}")
    private String tracestackEnvRange;

    @ExceptionHandler(Exception.class)
    public ReturnResult<Object> exceptionHandler(Exception e) {
        log.error("错误信息为：", e);
        ReturnResult<Object> error = ReturnResult.error(ResponseEnums.SYS_EXCEPTION.getRetCode(), ResponseEnums.SYS_EXCEPTION.getMessage());
        if (e.getClass() == ConstraintViolationException.class) {
            log.error("参数校验错误");
            ConstraintViolationException constraintViolationException = (ConstraintViolationException)e;

            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                message.append(constraintViolation.getMessage()).append(";");
            }
            error.setRetCode(ResponseEnums.ARGUMENT_EXCEPTION.getRetCode());
            error.setMessage(message.toString());
        }else if (e.getClass() == MethodArgumentNotValidException.class) {
            log.error("参数校验错误");
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException)e;
            FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
            String errorMessage = fieldError!=null ? fieldError.getDefaultMessage() : null;
            error.setRetCode(ResponseEnums.ARGUMENT_EXCEPTION.getRetCode());
            error.setMessage(errorMessage);
        }else if (e.getClass() == CommonException.class) {
            CommonException commonException = (CommonException)e;
            log.error("业务异常，错误信息为：{}", commonException.getMsg());
            error.setRetCode(commonException.getCode());
            error.setMessage(commonException.getMsg());
        }else{
            error = ReturnResult.error(ResponseEnums.SYS_EXCEPTION.getRetCode(), ResponseEnums.SYS_EXCEPTION.getMessage());
        }
        isOutputTracestackInfo(e, error);
        return error;
    }

    public void isOutputTracestackInfo(Throwable t, ReturnResult<Object> error) {
        // 是否打印堆栈信息
        String[] tracestackEnvRangeSplit = tracestackEnvRange.split(",");
        if(StringUtils.equalsAnyIgnoreCase(env.getProperty(SPRING_PROFILES_ACTIVE),tracestackEnvRangeSplit)){
            error.setTracestack(printStackTraceToString(t));
        }
    }

    @ExceptionHandler(Throwable.class)
    public Object throwableHandler(Throwable e) {
        return getObjectReturnResult(e);
    }

    private ReturnResult<Object> getObjectReturnResult(Throwable t) {
        log.error("ExceptionHandler 拦截异常信息：", t);
        ReturnResult<Object> error = ReturnResult.error("509","请求操作执行异常，请稍后再试。");
        isOutputTracestackInfo(t, error);
        return error;
    }

    public String printStackTraceToString(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw, true));
        return sw.getBuffer().toString();
    }
}
