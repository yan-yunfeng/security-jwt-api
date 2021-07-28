package com.yan.dao.impl;

import com.yan.dao.MysqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author : 鄢云峰
 */
@Repository
public class MysqlDaoImpl implements MysqlDao {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;

    @Override
    public Object findBySql(String sql) {
        return mysqlJdbcTemplate.queryForList(sql);
    }
}
