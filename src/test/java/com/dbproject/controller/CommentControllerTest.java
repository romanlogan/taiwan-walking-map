package com.dbproject.controller;

import com.dbproject.api.comment.CreateCommentRequest;
import com.dbproject.api.comment.dto.DeleteCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.comment.CommentService;
import com.dbproject.api.member.MemberService;
import com.dbproject.web.comment.CommentController;
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

import java.util.List;

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
    void saveComment() throws Exception {

        //given
        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content,rate);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk());
     }


    @DisplayName("未登入的使用者儲存comment時，return 401 UnAuthorization Error")
    @Test
    void saveCommentWithoutLogin() throws Exception {

        //given
        Integer rate = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content,rate);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/createComment")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());


        //then

    }


    @DisplayName("save comment時locationId值是必要")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveCommentWithBlankLocationId() throws Exception {

        //given
        Integer rate = 5;
        String content = "content1";
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value(" "));
    }

    @DisplayName("save comment時如果Request裡locationId只有19個字，就return Bad_Request Error")
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value("abcdeabcdeabcdeabcd"));
    }


    @DisplayName("save comment時如果Request裡locationId超過20個字，就return Bad_Request Error")
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value("abcdeabcdeabcdeabcdea"));
    }


    @DisplayName("save comment時content值是必要")
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
                .andExpect(jsonPath("$.messageList").value("content值是必要"))
                .andExpect(jsonPath("$.dataList").value(" "));
    }


    @DisplayName("save comment時content只能最多255字")
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
                .andExpect(jsonPath("$.messageList").value("content只能最多255字"))
                .andExpect(jsonPath("$.dataList").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }

    @DisplayName("save comment時Rate值是必要")
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
                .andExpect(jsonPath("$.messageList").value("rate值是必要"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList[0]").value(nullValue()));
    }

    @DisplayName("save comment時如果rate超過5，就return Bad_Request Error")
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(6));
    }


    @DisplayName("save comment時如果rate值是0，就return Bad_Request Error")
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(0));
    }

    @DisplayName("save comment時如果rate值是負數，就return Bad_Request Error")
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(-1));
    }



    @DisplayName("save comment時locationId值和content是必要")
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

//                .andExpect(jsonPath("$.messageList").value(List.of("locationId要20個字","content值是必要")))
//                .andExpect(jsonPath("$.messageList[0]").value("content值是必要")) //순서가 랜덤
                .andExpect(jsonPath("$.messageList").isArray()) //순서가 랜덤
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(2))) //순서가 랜덤
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字"))) //순서가 랜덤
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("content值是必要")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(" "))) ;//순서가 랜덤
//                .andExpect(jsonPath("$.dataList").value(List.of(" ", " ")));
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
                .andExpect(jsonPath("$.messageList").isArray())
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("rate只能從1點到五點的值")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("abcdeabcdeabcdeabcd")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(6)));
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
                .andExpect(jsonPath("$.messageList").isArray())
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("rate只能從1點到五點的值")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("content只能最多255字")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("abcdeabcdeabcdeabcd")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(6)))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid")));
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value(" "));
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value("abcdeabcdeabcdeabcd"));
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
                .andExpect(jsonPath("$.messageList").value("locationId要20個字"))
                .andExpect(jsonPath("$.dataList").value("abcdeabcdeabcdeabcdea"));
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
                .andExpect(jsonPath("$.messageList").value("content值是必要"))
                .andExpect(jsonPath("$.dataList").value(" "));
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
                .andExpect(jsonPath("$.messageList").value("content只能最多255字"))
                .andExpect(jsonPath("$.dataList").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
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
                .andExpect(jsonPath("$.messageList").value("rate值是必要"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList[0]").value(nullValue()));
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(6));
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(0));
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
                .andExpect(jsonPath("$.messageList").value("rate只能從1點到五點的值"))
//                .andExpect(jsonPath("$.dataList").value(List.of(nullValue())));
                .andExpect(jsonPath("$.dataList").value(-1));
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
                .andExpect(jsonPath("$.messageList").isArray())
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("content值是必要")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(" "))) ;
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
                .andExpect(jsonPath("$.messageList").isArray())
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("rate只能從1點到五點的值")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("abcdeabcdeabcdeabcd")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(6)));
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
                .andExpect(jsonPath("$.messageList").isArray())
                .andExpect(jsonPath("$.messageList", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("rate只能從1點到五點的值")))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("content只能最多255字")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("abcdeabcdeabcdeabcd")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(6)))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid")));
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
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("commentId不能低於1")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(0)));
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
}

















