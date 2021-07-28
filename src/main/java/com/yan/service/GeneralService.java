package com.yan.service;

import com.yan.model.Result;

/**
 * @author : 鄢云峰
 */
public interface GeneralService {

    /**
     * 根据sql从mysql中查询数据
     */
    Result findFromMysql(String sql) throws Throwable;

}
