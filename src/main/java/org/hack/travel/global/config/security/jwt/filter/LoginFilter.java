package org.hack.travel.global.config.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import lombok.AllArgsConstructor;
import org.hack.travel.global.config.mapper.ObjectMapperConfig;
import org.hack.travel.global.config.security.jwt.entity.RefreshEntity;
import org.hack.travel.global.config.security.jwt.repository.RefreshRepository;
import org.hack.travel.global.config.security.jwt.util.JwtUtil;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        String email = obtainUsername(request);
        String password = obtainPassword(request);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            email, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authentication)
        throws IOException {
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", email, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        addRefreshEntity(email, refresh, 86400000L);

        ObjectMapper objectMapper = new ObjectMapperConfig().objectMapper();

        var responseResult = ResponseResult.success(MessageType.LOGIN_SUCCESS, null);
        String responseBody = objectMapper.writeValueAsString(responseResult);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("access", access);
        response.setCharacterEncoding("UTF-8");
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(response.getStatus());
        response.getWriter().write(responseBody);
        response.getWriter().flush();
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true); Https 통신 진행할 경우 이 값을 넣어주면 됨
        //cookie.setPath("/"); 쿠키가 적용될 범위 설정을 위함
        cookie.setHttpOnly(true);
        return cookie;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }
}
