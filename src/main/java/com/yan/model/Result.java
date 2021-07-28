package com.yan.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : 鄢云峰
 */
@Data
public class Result implements Serializable {

    private String code;
    private Object data;
    private String msg;
    private long time;

    private Result(String code, Object data, String msg, long time) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.time = time;
    }

    public static Result ofSuccess(Object data) {
        return new Result(ResultModel.SUCCESS.code(), data, ResultModel.SUCCESS.msg(), System.currentTimeMillis());
    }

    public static Result ofFailure(Throwable t) {
        return new Result(ResultModel.QUERY_ERROR.code(), null, t.getMessage(), System.currentTimeMillis());
    }
    public static Result ofFailure(String msg) {
        return new Result(ResultModel.QUERY_ERROR.code(), null, msg, System.currentTimeMillis());
    }

}
