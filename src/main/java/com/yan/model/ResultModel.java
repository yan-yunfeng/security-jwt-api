package com.yan.model;

/**
 * @author : 鄢云峰
 */
public enum ResultModel {

    /**
     * 成功返回
     */
    SUCCESS("0", "success"),

    /**
     * 参数为空或者异常返回
     */
    ARGS_ERROR("0001", "参数为空或结构非约定结构！[0001]"),

    /**
     * 查询异常返回
     */
    QUERY_ERROR("0002", "暂无数据，请稍后重试！[0002]"),

    /**
     * 权限异常返回
     */
    AUTH_ERROR("0003", "权限错误，请申请token访问！[0003]"),

    /**
     * Json格式异常返回
     */
    JSON_ERROR("0004", "参数非标准的JSON格式！[0004]");


    private String code;
    private String msg;

    ResultModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
