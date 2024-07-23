package org.hack.travel.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.hack.travel.global.config.mapper.ObjectMapperConfig;
import org.hack.travel.global.config.security.jwt.filter.LoginFilter;
import org.hack.travel.global.config.security.jwt.repository.RefreshRepository;
import org.hack.travel.global.config.security.jwt.util.JwtUtil;
import org.hack.travel.global.exception.type.MessageType;
import org.hack.travel.global.response.ResponseResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class LoginFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshRepository refreshRepository;

    @InjectMocks
    private LoginFilter loginFilter;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loginFilter = new LoginFilter(authenticationManager, jwtUtil, refreshRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(new Object()).addFilter(loginFilter).build();
    }

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String role = "ROLE_USER";
        String accessToken = "access-token";
        String refreshToken = "refresh-token";

        GrantedAuthority authority = new SimpleGrantedAuthority(role);
        Authentication auth = new UsernamePasswordAuthenticationToken(email, password,
            Collections.singletonList(authority));

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.createJwt("access", email, role, 600000L)).thenReturn(accessToken);
        when(jwtUtil.createJwt("refresh", email, role, 86400000L)).thenReturn(refreshToken);

        RequestBuilder request = MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", email)
            .param("password", password);

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(header().string("access", accessToken))
            .andExpect(cookie().value("refresh", refreshToken))
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().json(new ObjectMapperConfig().objectMapper()
                .writeValueAsString(ResponseResult.success(MessageType.LOGIN_SUCCESS, null))));

        verify(refreshRepository).save(any());
    }

    @Test
    public void testUnsuccessfulAuthentication() throws Exception {
        when(authenticationManager.authenticate(any())).thenThrow(
            new AuthenticationServiceException("Authentication failed"));

        RequestBuilder request = MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("username", "test@example.com")
            .param("password", "wrong-password");

        mockMvc.perform(request)
            .andExpect(status().isUnauthorized());

        verify(refreshRepository, never()).save(any());
    }
}

