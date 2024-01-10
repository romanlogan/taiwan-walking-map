package com.dbproject.controller;

import com.dbproject.dto.CreateCommentRequest;
import com.dbproject.dto.RegisterFormDto;
import com.dbproject.entity.Member;
import com.dbproject.repository.MemberRepository;
import com.dbproject.service.CommentService;
import com.dbproject.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;


    @DisplayName("신규 댓글을 등록한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test() throws Exception {

        //given
        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rating);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());
     }


    @DisplayName("비 로그인으로 신규 댓글을 등록을 등록시 401 UnAuthorization 에러가 발생합니다")
    @Test
    void test2() throws Exception {

        //given
        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rating);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
//                .andExpect(jsonPath("$.code").value(403))
//                .andExpect(jsonPath("$.status").value("FORBIDDEN"));

        //then

    }



    @DisplayName("댓글 작성시 content 는 필수 값 입니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test3() throws Exception {

        //given
        int rating = 5;
        String content = "";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rating);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("댓글 작성시 rating 는 필수 값 입니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test4() throws Exception {

        //given
//        int rating = ;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, null);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}

















