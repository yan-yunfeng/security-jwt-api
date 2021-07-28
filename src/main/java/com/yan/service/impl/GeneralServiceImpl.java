package com.yan.service.impl;

import com.yan.dao.MysqlDao;
import com.yan.model.Result;
import com.yan.service.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 鄢云峰
 */
@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    private MysqlDao mysqlDao;

    @Override
    public Result findFromMysql(String sql) throws Throwable {
        return Result.ofSuccess(mysqlDao.findBySql(sql));
    }

}
