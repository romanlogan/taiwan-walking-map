package com.dbproject.web.friend;

import com.dbproject.api.friend.FriendService;
import com.dbproject.api.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = FriendController.class)
class FriendControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private FriendService friendService;

    @DisplayName("친구 요청을 저장합니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveFriendRequest() throws Exception{
        //given
        String email = "aaa@aaa.com";
        String memo = "memo 1";

        AddFriendRequest addFriendRequest = new AddFriendRequest(email, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
     }


    @DisplayName("비 로그인 유저가 친구 요청을 저장시 401 UnAuthorization 을 반환합니다")
    @Test
    void saveFriendRequestWithoutLogin() throws Exception{
        //given
        String email = "aaa@aaa.com";
        String memo = "memo 1";

        AddFriendRequest addFriendRequest = new AddFriendRequest(email, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("친구 요청을 저장시 email 은 Null 이 될 수 없습니다 ")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithNullEmail() throws Exception{
        //given
        String email = null;
        String memo = "memo 1";

        AddFriendRequest addFriendRequest = new AddFriendRequest(email, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("친구 요청을 저장시 email 은 ' ' 이 될 수 없습니다 ")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithBlankEmail() throws Exception{
        //given
        String email = " ";
        String memo = "memo 1";

        AddFriendRequest addFriendRequest = new AddFriendRequest(email, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("친구 요청을 저장시 email 은 이메일 형식이어야 합니다 ")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithoutEmailForm() throws Exception{
        //given
        String email = "asdasdf";
        String memo = "memo 1";

        AddFriendRequest addFriendRequest = new AddFriendRequest(email, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/members/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



}