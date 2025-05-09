package com.dbproject.web.friend;

import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.friend.dto.AcceptAddFriendRequest;
import com.dbproject.api.friend.dto.AddFriendRequest;
import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.service.FriendServiceImpl;
import com.dbproject.api.friend.friendRequest.dto.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.constant.FriendRequestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import java.util.Optional;

import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    private FriendServiceImpl friendService;

    @DisplayName("save friend request")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveFriendRequest() throws Exception{
        //given
        AddFriendRequest addFriendRequest = createAddFriendRequest("aaa@aaa.com", "memo 1");

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
     }

    private static AddFriendRequest createAddFriendRequest(String friendEmail, String memo) {
        return AddFriendRequest.create(friendEmail, memo);
    }

    @DisplayName("When a non-logged-in member saves a friend request, return 401 unAuthorization,")
    @Test
    void saveFriendRequestWithoutLogin() throws Exception{
        //given
        AddFriendRequest addFriendRequest = createAddFriendRequest("aaa@aaa.com", "memo 1");

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("When saving a friend request, email cannot be null.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithNullEmail() throws Exception{
        //given
        AddFriendRequest addFriendRequest = createAddFriendRequest(null, "memo 1");

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.friendEmail.message").value("friendEmail value is required"))
                .andExpect(jsonPath("$.errorMap.friendEmail.rejectedValue").value(nullValue()));
    }

    @DisplayName("When saving a friend request, email cannot be blank")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithBlankEmail() throws Exception{
        //given
        AddFriendRequest addFriendRequest = createAddFriendRequest(" ", "memo 1");

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.friendEmail.message").value("friendEmail value is required"))
                .andExpect(jsonPath("$.errorMap.friendEmail.rejectedValue").value(" "));
    }

//    이메일 검증을 나중에 아이디로 변경할 것
//    @DisplayName("When saving a friend request, the email must be in email format.")
//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    void canNotSaveFriendRequestWithoutEmailForm() throws Exception{
//        //given
//        AddFriendRequest addFriendRequest = createAddFriendRequest("asdasdf", "memo 1");
//
//        //when //then
//        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
//                        .with(csrf())
//                        .content(objectMapper.writeValueAsString(addFriendRequest))
//                        .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
//                .andExpect(jsonPath("$.errorMap.friendEmail.message").value("friendEmail value is required"))
//                .andExpect(jsonPath("$.errorMap.friendEmail.rejectedValue").value("asdasdf"));
//    }

    @DisplayName("When saving a friend request, the memo can only have a maximum of 255 characters")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void canNotSaveFriendRequestWithOverMemoSize() throws Exception{
        //given
        String memo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";
        AddFriendRequest addFriendRequest = createAddFriendRequest("asdf@asdf.com", memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/addFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.memo.message").value("memo can only have a maximum of 255 characters"))
                .andExpect(jsonPath("$.errorMap.memo.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }

    Page<RequestFriendListDto> getRequestFriendPage(Pageable pageable) {

        RequestFriendListDto requestFriendListDto = new RequestFriendListDto(1L, "asdf@asdf.com", "lee", FriendRequestStatus.WAITING);
        List<RequestFriendListDto> list = new ArrayList<>();
        list.add(requestFriendListDto);

        return new PageImpl<>(list, pageable, 1);
    }
                                                              
    @DisplayName("get member's received friend request page")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getRequestFriendList() throws Exception{
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        Page<RequestFriendListDto> page = getRequestFriendPage(pageable);
        Mockito.when(friendService.getRequestFriendList(pageable, "qwer@qwer.com")).thenReturn(page);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/requestFriendList"))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("When Non-logged-in members access to get request friend ist, return 401 unAuthorized")
    @Test
    void getRequestFriendListWithoutLogin() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/requestFriendList"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("accept friend request")
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

    @DisplayName("When Non-logged-in members access to accept friend requests, return 401 unAuthorized")
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


    @DisplayName("When accepting a friend request, friendRequestId can't be null.")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void acceptAddFriendRequestCanNotWithNullFriendRequestId() throws Exception{
        //given
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(null);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post("/myPage/acceptAddFriend")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptAddFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorMap.friendRequestId.message").value("friendRequestId value is required"))
                .andExpect(jsonPath("$.errorMap.friendRequestId.rejectedValue").value(nullValue()));
    }

    @DisplayName("reject friend request")
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

    @DisplayName("When Non-logged-in members access to reject friend requests, return 401 unAuthorized")
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

    @DisplayName("When reject a friend request, friendRequestId can't be null.")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void friendRequestIdCanNotNullInRejectFriendRequest() throws Exception{
        //given
        RejectFriendRequest deleteFriendRequest = new RejectFriendRequest(null);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/myPage/rejectFriendRequest")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFriendRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMap.friendRequestId.message").value("friendRequestId value is required"))
                .andExpect(jsonPath("$.errorMap.friendRequestId.rejectedValue").value(nullValue()));
    }

    @DisplayName("get member's friend list")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFriendList() throws Exception{

        Mockito.when(friendService.getFriendList("qwer@qwer.com")).thenReturn(getFriendListResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/friendList"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static FriendListResponse getFriendListResponse() {
        List<FriendDto> friendDtoList = new ArrayList<>();
        List<FavoriteLocationDto> favoriteLocationList = new ArrayList<>();
        return FriendListResponse.create(friendDtoList, favoriteLocationList);
    }

    @DisplayName("When Non-logged-in members access to reject friend requests, return 401 unAuthorized")
    @Test
//    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFriendListWithoutLogin() throws Exception{

        Mockito.when(friendService.getFriendList("qwer@qwer.com")).thenReturn(getFriendListResponse());

        mockMvc.perform(MockMvcRequestBuilders.get("/myPage/friendList"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}