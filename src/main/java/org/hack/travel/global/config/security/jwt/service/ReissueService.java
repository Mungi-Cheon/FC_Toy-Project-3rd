package org.hack.travel.global.config.security.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.AllArgsConstructor;
import org.hack.travel.global.config.security.jwt.entity.RefreshEntity;
import org.hack.travel.global.config.security.jwt.repository.RefreshRepository;
import org.hack.travel.global.config.security.jwt.util.JwtUtil;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ResponseResult<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return ResponseResult.error(MessageType.REFRESH_TOKEN_IS_NULL, null);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseResult.error(MessageType.EXPIRED_REFRESH_TOKEN, null);
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            return ResponseResult.error(MessageType.INVALID_REFRESH_TOKEN, null);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            return ResponseResult.error(MessageType.INVALID_REFRESH_TOKEN, null);
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", email, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        refreshRepository.deleteByRefresh(refresh);
        addRefreshEntity(email, newRefresh);

        response.setHeader("access", newAccess);
        response.addCookie(createCookie(newRefresh));
        return ResponseResult.success(MessageType.REPUBLISH_TOKEN, null);
    }

    private void addRefreshEntity(String email, String refresh) {
        Date date = new Date(System.currentTimeMillis() + 86400000L);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setEmail(email);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());
        refreshRepository.save(refreshEntity);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        return cookie;
    }
}