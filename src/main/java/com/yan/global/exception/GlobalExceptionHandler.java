package com.yan.global.exception;

import com.yan.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常的处理类
 *
 * @author : 鄢云峰
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 全局的异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public Result handleGlobalException(Throwable t) {
        return Result.ofFailure(t);
    }

}
