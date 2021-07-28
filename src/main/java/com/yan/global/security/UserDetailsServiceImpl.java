package com.yan.global.security;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

/**
 * 通过用户名查询用户信息的service
 *
 * @author : 鄢云峰
 */
@Component
@Qualifier("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @Qualifier("securityJdbcTemplate")
    private JdbcTemplate securityJdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //这里可以直接查询出map，或者新建一个用户类实现UserDetails，为了加深其他应用，这里使用jsonObject接收
        //Map<String,Object> resultMap = securityJdbcTemplate.queryForMap("select * from user where username = "+username);

        JSONObject userObject =
                securityJdbcTemplate.query(
                        "select * from user where username = \""+username+"\"",
                        //查询结果Result转为实体类的解析函数
                        new ResultSetExtractor<JSONObject>() {
                            @Override
                            public JSONObject extractData(ResultSet rs) throws SQLException, DataAccessException {
                                if (rs.first()) {
                                    ResultSetMetaData metaData = rs.getMetaData();
                                    int count = metaData.getColumnCount();
                                    JSONObject jsonObject = new JSONObject();
                                    for (int i = 1; i <= count; i++) {
                                        String columnName = metaData.getColumnName(i);
                                        Object value = rs.getObject(i);
                                        jsonObject.put(columnName, value);
                                    }
                                    return jsonObject;
                                }
                                return null;
                            }
                        });

        if(userObject!=null){
            String password = userObject.getString(SecurityKeys.PASSWORD);
            String authorities = userObject.getString(SecurityKeys.AUTHORITIES);
            Collection<? extends GrantedAuthority> authorityList = Collections.emptyList();
            if (authorities!=null) {
                authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
            }
            return new User(username, password, authorityList);
        }
        return null;
    }

}
