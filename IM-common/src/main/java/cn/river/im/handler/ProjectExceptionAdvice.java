package cn.river.im.handler;

import cn.river.im.enums.ApiCode;
import cn.river.im.exception.SystemException;
import cn.river.im.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProjectExceptionAdvice {
    /**
     * 请求异常
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> httpRequestMethodNotSupportedExceptionHandler(Exception exception) {
        return Result.fail(ApiCode.HTTP_REQUEST_METHOD_NOT_SUPPORTED_EXCEPTION.getCode(), exception.getMessage());
    }

    /**
     * 默认的异常处理
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> exceptionHandler(Exception exception) {
        return  Result.fail(ApiCode.SYSTEM_EXCEPTION.getCode(), ApiCode.SYSTEM_EXCEPTION.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(value = SystemException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Boolean> systemExceptionHandler(SystemException systemException){
        log.error("异常信息：{}，异常来源：{}",systemException.getMessage());
        return Result.fail(systemException.getCode(),systemException.getMessage());
    }
}
