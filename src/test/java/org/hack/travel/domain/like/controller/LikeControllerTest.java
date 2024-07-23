package org.hack.travel.domain.like.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hack.travel.domain.like.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = LikeController.class)
@ExtendWith(MockitoExtension.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Mock
    private Authentication authentication;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String USER_NAME = "test1@gmail.com";
    private static final int TRIP_ID = 1;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void like_Success() throws Exception {
        when(authentication.getName()).thenReturn(USER_NAME);
        mockMvc.perform(post("/api/trips/{trip-id}/likes", TRIP_ID)
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.resultCode").value(201))
            .andExpect(jsonPath("$.resultMessages[0]").value("여행 정보에 좋아요를 눌렀습니다."))
            .andExpect(jsonPath("$.data").isEmpty());

        verify(likeService).like(TRIP_ID, USER_NAME);
    }
}