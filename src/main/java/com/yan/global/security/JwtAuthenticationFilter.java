package com.yan.global.security;

import com.alibaba.fastjson.JSONObject;
import com.yan.model.Result;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 获取token后的每次请求的验证，这里可以直接继承GenericFilterBean
 *
 * @author : 鄢云峰
 */
public class JwtAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            Claims claims = JwtTokenUtil.getClaims((HttpServletRequest) servletRequest);
            //获取用户名
            String username = claims.getSubject();
            //获取权限信息
            String authorities = (String) claims.get(SecurityKeys.AUTHORITIES);
            List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            wrongToken(servletResponse, e);
        }
    }

    private void wrongToken(ServletResponse servletResponse, Exception e) throws IOException {
        //这里必须要设置contentType，否则返回的数据可能为乱码
        servletResponse.setContentType("application/json;charset=UTF-8");
        servletResponse.getWriter().write(JSONObject.toJSONString(Result.ofFailure("无效的token")));
    }

}
