package com.dbproject.controller;

import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.DeleteCommentRequest;
import com.dbproject.api.comment.dto.GetNextCommentListRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.comment.service.CommentServiceImpl;
import com.dbproject.api.member.MemberService;
import com.dbproject.web.comment.CommentController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberRepository memberRepository;


    @DisplayName("add new comment")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveComment() throws Exception {

        //given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("C1_379000000A_001572", "댓글1 입니다.", 5);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());
     }


    @DisplayName("When a non-logged-in member access to save comment, return 401 unAuthorization,")
    @Test
    void saveCommentWithoutLogin() throws Exception {

        //given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest("C1_379000000A_001572", "댓글1 입니다.", 5);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @DisplayName("When saving comment, locationId value can't be null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNullLocationId() throws Exception {

        //given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "content1", 5);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId value is required"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(nullValue()));
    }

    @DisplayName("When saving comment, locationId value can't be blank")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithBlankLocationId() throws Exception {

        //given
        String email = "zxcv@zxcv.com";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(" ", "content1", 5);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(" "));
    }

    @DisplayName("When saving comment, if the locationId in Request is only 19 words, return Bad_Request")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNotFormattedLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd";  //19

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"));
    }


    @DisplayName("When saving comment, if the locationId in Request exceeds 20 words, return Bad_Request")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithOverNumberOfCharactersLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcdea";  //21

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcdea"));

    }


    @DisplayName("When saving comment, content value can't be blank")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithBlankContent() throws Exception {

        //given
        Integer rate = 5;
        String content = " ";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value is required"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value(" "));
    }


    @DisplayName("When saving comment, content value can only be up to 255 words")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithOverContentSize() throws Exception {

        //given
        Integer rate = 5;
        //256個字
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value can only be up to 255 words"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }

    @DisplayName("When saving comment, rate value can't be null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNullRate() throws Exception {

        //given
//        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, null);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate value is required"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(nullValue()));

    }

    @DisplayName("When saving comment, rate value can't exceeds 5 points")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithOverRate() throws Exception {

        //given
        Integer rate = 6;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6));
    }


    @DisplayName("When saving comment, rate value can't be 0")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithZeroPointRate() throws Exception {

        //given
        Integer rate = 0;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(0));
    }

    @DisplayName("When saving comment, rate value can't be negative")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNegativePointRate() throws Exception {

        //given
        Integer rate = -1;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(-1));
    }



    @DisplayName("When saving comment, locationId value and content value can't be blank")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithBlankLocationIdAndBlankContent() throws Exception {

        //given
        Integer rate = 5;
        String content = " ";
        String email = "zxcv@zxcv.com";
        String locationId = " ";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(" "))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value is required"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value(" "));
    }



    @DisplayName("save comment時locationId值是20字，rate值是從要1到5以內的值")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNotFormattedLocationIdAndNotFormattedRating() throws Exception {

        //given
        Integer rate = 6;
        String content = "content";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6));
    }

    @DisplayName("save comment時locationId值要20字，rate值是從要1到5以內的值，content不能超過255個字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithNotFormattedLocationIdAndNotFormattedRatingAndOverContent() throws Exception {

        //given
        Integer rate = 6;
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd"; //19

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value can only be up to 255 words"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }

    /**
     * update comment
     */

    @DisplayName("update comment")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateComment() throws Exception {

        //given
        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());
    }


    @DisplayName("未登入的使用者update comment時，return 401 UnAuthorization Error")
    @Test
    void updateCommentWithoutLogin() throws Exception {

        //given
        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }


    @DisplayName("update comment時locationId值是必要")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithBlankLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
        String email = "zxcv@zxcv.com";
        String locationId = " ";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(" "));

    }

    @DisplayName("update comment時如果Request裡locationId只有19個字，就return Bad_Request Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithNotFormattedLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd";  //19

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"));

    }


    @DisplayName("update comment時如果Request裡locationId超過20個字，就return Bad_Request Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithOverNumberOfCharactersLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcdea";  //21

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcdea"));
    }


    @DisplayName("update comment時content值是必要")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithBlankContent() throws Exception {

        //given
        Integer rate = 5;
        String content = " ";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value is required"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value(" "));
    }


    @DisplayName("update comment時content只能最多255字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithOverContentSize() throws Exception {

        //given
        Integer rate = 5;
        //256個字
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value can only be up to 255 words"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }

    @DisplayName("update comment時Rate值是必要")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithNullRate() throws Exception {

        //given
//        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, null);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate value is required"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(nullValue()));
    }

    @DisplayName("update comment時如果rate超過5，就return Bad_Request Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithOverRate() throws Exception {

        //given
        Integer rate = 6;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6));
    }


    @DisplayName("update comment時如果rate值是0，就return Bad_Request Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithZeroPointRate() throws Exception {

        //given
        Integer rate = 0;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(0));

    }

    @DisplayName("update comment時如果rate值是負數，就return Bad_Request Error")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithNegativePointRate() throws Exception {

        //given
        Integer rate = -1;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(-1));
    }



    @DisplayName("update comment時locationId值和content是必要")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithBlankLocationIdAndBlankContent() throws Exception {

        //given
        Integer rate = 5;
        String content = " ";
        String email = "zxcv@zxcv.com";
        String locationId = " ";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(" "))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value is required"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value(" "));

    }



    @DisplayName("update comment時locationId值是20字，rate值是從要1到5以內的值")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithNotFormattedLocationIdAndNotFormattedRating() throws Exception {

        //given
        Integer rate = 6;
        String content = "content";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd";

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6));
    }

    @DisplayName("update comment時locationId值要20字，rate值是從要1到5以內的值，content不能超過255個字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateCommentWithNotFormattedLocationIdAndNotFormattedRatingAndOverContent() throws Exception {

        //given
        Integer rate = 6;
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";
        String email = "zxcv@zxcv.com";
        String locationId = "abcdeabcdeabcdeabcd"; //19

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest(locationId, content, rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/comment/updateComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"))
                .andExpect(jsonPath("$.errorMap.rate.message").value("rate can only have values from 1 to 5 points"))
                .andExpect(jsonPath("$.errorMap.rate.rejectedValue").value(6))
                .andExpect(jsonPath("$.errorMap.content.message").value("content value can only be up to 255 words"))
                .andExpect(jsonPath("$.errorMap.content.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));

    }


    @DisplayName("刪除已儲存的comment")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteComment() throws Exception {

        //given
        Integer commentId = 1;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/deleteComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());
    }

    @DisplayName("刪除comment時，commentId不能0")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteCommentWithZeroCommentId() throws Exception {

        //given
        Integer commentId = 0;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/deleteComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.commentId.message").value("commentId不能低於1"))
                .andExpect(jsonPath("$.errorMap.commentId.rejectedValue").value(0));

    }

    @DisplayName("未登入的使用者刪除comment時，return 401 UnAuthorization Error")
    @Test
//    @WithMockUser(username = "user", roles = "USER")
    void deleteCommentWithNegativeCommentId() throws Exception {

        //given
        Integer commentId = 1;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/deleteComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("")
    @Test
    void getNextCommentList() throws Exception {

        GetNextCommentListRequest request = new GetNextCommentListRequest("C1_379000000A_001572",1);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/comment/getNextCommentList")
                        .param("locationId","C1_379000000A_001572")
                        .param("page","1")
                )
                .andDo(print())
                .andExpect(status().isOk());

    }
}

















