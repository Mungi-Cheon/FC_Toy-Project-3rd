package org.hack.travel.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hack.travel.global.config.security.jwt.repository.RefreshRepository;
import org.hack.travel.global.config.security.jwt.service.ReissueService;
import org.hack.travel.global.config.security.jwt.util.JwtUtil;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReissueServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshRepository refreshRepository;

    @InjectMocks
    private ReissueService reissueService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReissue_Success() {
        String refreshToken = "valid-refresh-token";
        Cookie[] cookies = {new Cookie("refresh", refreshToken)};

        when(request.getCookies()).thenReturn(cookies);
        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getCategory(refreshToken)).thenReturn("refresh");
        when(refreshRepository.existsByRefresh(refreshToken)).thenReturn(true);
        when(jwtUtil.getEmail(refreshToken)).thenReturn("test@example.com");
        when(jwtUtil.getRole(refreshToken)).thenReturn("USER");
        when(jwtUtil.createJwt(anyString(), anyString(), anyString(), anyLong()))
            .thenReturn("new-access-token")
            .thenReturn("new-refresh-token");

        ResponseResult<?> result = reissueService.reissue(request, response);

        assertEquals(MessageType.REPUBLISH_TOKEN.getMessage(), result.getResultMessages().get(0));
        verify(refreshRepository, times(1)).deleteByRefresh(refreshToken);
        verify(refreshRepository, times(1)).save(any());
    }

    @Test
    public void testReissue_RefreshTokenIsNull() {
        when(request.getCookies()).thenReturn(new Cookie[]{});

        ResponseResult<?> result = reissueService.reissue(request, response);

        assertEquals(MessageType.REFRESH_TOKEN_IS_NULL.getMessage(),
            result.getResultMessages().get(0));
        verify(refreshRepository, never()).existsByRefresh(anyString());
    }

    @Test
    public void testReissue_ExpiredRefreshToken() {
        String refreshToken = "expired-refresh-token";
        Cookie[] cookies = {new Cookie("refresh", refreshToken)};

        when(request.getCookies()).thenReturn(cookies);
        doThrow(new ExpiredJwtException(null, null, null)).when(jwtUtil).isExpired(refreshToken);

        ResponseResult<?> result = reissueService.reissue(request, response);

        assertEquals(MessageType.EXPIRED_REFRESH_TOKEN.getMessage(),
            result.getResultMessages().get(0));
        verify(refreshRepository, never()).existsByRefresh(anyString());
    }

    @Test
    public void testReissue_InvalidCategory() {
        String refreshToken = "invalid-category-refresh-token";
        Cookie[] cookies = {new Cookie("refresh", refreshToken)};

        when(request.getCookies()).thenReturn(cookies);
        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getCategory(refreshToken)).thenReturn("access");

        ResponseResult<?> result = reissueService.reissue(request, response);

        assertEquals(MessageType.INVALID_REFRESH_TOKEN.getMessage(),
            result.getResultMessages().get(0));
        verify(refreshRepository, never()).existsByRefresh(anyString());
    }

    @Test
    public void testReissue_InvalidRefreshToken() {
        String refreshToken = "nonexistent-refresh-token";
        Cookie[] cookies = {new Cookie("refresh", refreshToken)};

        when(request.getCookies()).thenReturn(cookies);
        when(jwtUtil.isExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.getCategory(refreshToken)).thenReturn("refresh");
        when(refreshRepository.existsByRefresh(refreshToken)).thenReturn(false);

        ResponseResult<?> result = reissueService.reissue(request, response);

        assertEquals(MessageType.INVALID_REFRESH_TOKEN.getMessage(),
            result.getResultMessages().get(0));
    }
}

