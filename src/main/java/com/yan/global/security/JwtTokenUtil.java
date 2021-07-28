package com.yan.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

/**
 * @author : 鄢云峰
 */
public class JwtTokenUtil {

    private static final String SECRET = Base64.encodeBase64String("abc".getBytes());
    private static final Long EXPIRATION_TIME = 360000L;

    static String createToken(String username, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setSubject(username)
                .claim(SecurityKeys.AUTHORITIES,String.join(",", AuthorityUtils.authorityListToSet(authorities)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    static Claims getClaims(HttpServletRequest request) {
        String token = request.getHeader(SecurityKeys.AUTHORIZATION);
        if (token == null) {
            throw  new IllegalArgumentException("无效的token");
        }
        token = token.replace(SecurityKeys.BEARER_WITH_BLANK, "");
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

}
