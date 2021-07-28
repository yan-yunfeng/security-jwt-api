package com.yan.global.security;

import com.alibaba.fastjson.JSONObject;
import com.yan.model.Result;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : 鄢云峰
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    /**
     * 从请求体中获取用户名和密码信息,并进行认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        String username = request.getParameter(SecurityKeys.USERNAME);
        String password = request.getParameter(SecurityKeys.PASSWORD);
        //这里调用AuthenticationManager来验证用户名和密码
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }

    /**
     * 认证成功后，生成token，消息头中返回
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String jwtToken = JwtTokenUtil.createToken(authResult.getName(),authResult.getAuthorities());
        response.addHeader(SecurityKeys.AUTHORIZATION, SecurityKeys.BEARER_WITH_BLANK + jwtToken);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jwtToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(Result.ofFailure(new UsernameNotFoundException("未查到该用户"))));
    }
}
