package com.dbproject.web.friend;

import com.dbproject.api.friend.AcceptAddFriendRequest;
import com.dbproject.api.friend.AddFriendRequest;
import com.dbproject.api.friend.FriendService;
import com.dbproject.api.friend.friendRequest.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.RequestFriendListDto;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.constant.FriendRequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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
    private MemberRepository memberRepository;

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
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    Page<RequestFriendListDto> getRequestFriendPage(Pageable pageable) {

        RequestFriendListDto requestFriendListDto = new RequestFriendListDto(1L, "asdf@asdf.com", "lee", FriendRequestStatus.WAITING);
        List<RequestFriendListDto> list = new ArrayList<>();
        list.add(requestFriendListDto);

        return new PageImpl<>(list, pageable, 1);
    }

    @DisplayName("받은 친구 요청 페이지를 조회합니다.")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getRequestFriendList() throws Exception{
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        Page<RequestFriendListDto> page = getRequestFriendPage(pageable);
        Mockito.when(friendService.getRequestFriendList(pageable, "qwer@qwer.com")).thenReturn(page);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/myPage/requestFriendList")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("친구 요청을 수락합니다")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void acceptAddFriendRequest() throws Exception{
        //given
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(1L);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/acceptAddFriend")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptAddFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
     }

    @DisplayName("비 로그인 유저는 친구 요청 수락에 접근 할 수 없습니다.")
    @Test
    void acceptAddFriendRequestWithoutLogin() throws Exception{
        //given
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(1L);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/acceptAddFriend")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptAddFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("친구 요청을 수락시 friendRequestId 는 null 이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void acceptAddFriendRequestCanNotWithNullFriendRequestId() throws Exception{
        //given

        String friendRequestId = null;
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(null);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/acceptAddFriend")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptAddFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("친구 요청을 거절합니다.")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void rejectFriendRequest() throws Exception{
        //given
        RejectFriendRequest deleteFriendRequest = new RejectFriendRequest(1L);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/myPage/rejectFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
     }

    @DisplayName("비로그인 유저는 친구 요청을 거절에 접근 할 수 없습니다")
    @Test
    void rejectFriendRequestWithoutLogin() throws Exception{
        //given
        RejectFriendRequest deleteFriendRequest = new RejectFriendRequest(1L);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/myPage/rejectFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("친구 요청을 거절시 deleteFriendRequest 는 null 이 될 수 없습니다")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void rejectFriendRequestCanNotNullDeleteFriendRequest() throws Exception{
        //given
        RejectFriendRequest deleteFriendRequest = new RejectFriendRequest(null);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/myPage/rejectFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }


}