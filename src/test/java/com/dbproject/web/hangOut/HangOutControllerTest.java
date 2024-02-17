package com.dbproject.web.hangOut;

import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRequest;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutService;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.*;
import com.dbproject.web.friend.FriendController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HangOutController.class)
class HangOutControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private InviteHangOutService inviteHangOutService;

    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장합니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendList() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("비 로그인 유저가 친구 리스트 에서 온 HangOut 요청을 저장시 UnAuthorization 을 반환합니다")
    @Test
    void inviteFromFriendListWithoutLogin() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장시 favoriteLocationId 는 null 이 될 수 없습니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendListWithNullFavoriteLocationId() throws Exception {

        Long favoriteLocationId = null;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장시 departDateTime 는 null 이 될 수 없습니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendListWithNullDepartDateTime() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = null;
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장시 FriendEmail 는 Null 이 될 수 없습니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendListWithNullFriendEmail() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = null;

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장시 FriendEmail 는 Blank 가 될 수 없습니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendListWithBlankFriendEmail() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = " ";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("친구 리스트 에서 온 HangOut 요청을 저장시 FriendEmail 는 Empty 가 될 수 없습니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromFriendListWithEmptyFriendEmail() throws Exception {

        Long favoriteLocationId = 1L;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocationId, departDateTime, message, friendEmail);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromFriendList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장합니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPage() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("비 로그인 유저가 장소 페이지 에서 온 HangOut 요청을 저장시 unAuthorization 을 반환합니다")
    @Test
    void inviteFromLocationPageWithOutLogin() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 LocationId 는 null 이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithNullLocationId() throws Exception {

        String locationId = null;
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 LocationId 는 empty 가 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithEmptyLocationId() throws Exception {

        String locationId = "";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 LocationId 는 blank 가 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithBlankLocationId() throws Exception {

        String locationId = " ";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@email.com";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 friendEmail 는 null 이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithNullFriendEmail() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = null;

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 friendEmail 는 empty 가 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithEmptyFriendEmail() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 friendEmail 는 blank 가 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithBlankFriendEmail() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = " ";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장소 페이지 에서 온 HangOut 요청을 저장시 departDateTime 은 null이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void inviteFromLocationPageWithNull() throws Exception {

        String locationId = "abc123";
        LocalDateTime departDateTime = null;
        String message = "message 1";
        String friendEmail = " ";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(locationId, friendEmail, departDateTime, message);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/inviteFromLocationPage")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(inviteHangOutFromLocRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("받은 HangOut 요청 목록과 장소 디테일을 가져옵니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getInvitedHangOutListWithLocationDtl() throws Exception {

        List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();
        InviteHangOutLocationDto inviteHangOutLocationDto = new InviteHangOutLocationDto();
        InvitedHangOutResponse invitedHangOutResponse = new InvitedHangOutResponse(inviteHangOutLocationDto, invitedHangOutDtoList);

        Mockito.when(inviteHangOutService.getInvitedHangOutList("user" , Optional.of(1L))).thenReturn(invitedHangOutResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hangOut/invitedList")
                        .param("inviteHangOutId", String.valueOf(1L))
                )
                .andDo(print())
                .andExpect(status().isOk());
     }


    @DisplayName("받은 HangOut 요청 목록만 가져옵니다, 장소 디테일 없음")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getOnlyInvitedHangOutList() throws Exception {

        List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();
        InviteHangOutLocationDto inviteHangOutLocationDto = new InviteHangOutLocationDto();
        InvitedHangOutResponse invitedHangOutResponse = new InvitedHangOutResponse(inviteHangOutLocationDto, invitedHangOutDtoList);

        Mockito.when(inviteHangOutService.getInvitedHangOutList("user" , Optional.ofNullable(null))).thenReturn(invitedHangOutResponse);

        //given
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/hangOut/invitedList")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("친구 요청을 수락합니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void acceptInvitedHangOut() throws Exception {

        AcceptInvitedHangOutRequest acceptInvitedHangOutRequest = new AcceptInvitedHangOutRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/acceptInvitedHangOut")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptInvitedHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("친구 요청을 수락시 inviteHangOutId 는 null 이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void acceptInvitedHangOutWithNullInviteHangOutId() throws Exception {

        AcceptInvitedHangOutRequest acceptInvitedHangOutRequest = new AcceptInvitedHangOutRequest(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/hangOut/acceptInvitedHangOut")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(acceptInvitedHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("친구 요청을 거절합니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void rejectInvitedHangOut() throws Exception {

        RejectInvitedHangOutRequest rejectInvitedHangOutRequest = new RejectInvitedHangOutRequest(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/hangOut/rejectInvitedHangOut")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(rejectInvitedHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


    @DisplayName("친구 요청을 거절시 inviteHangOutId 는 null 이 될 수 없습니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void rejectInvitedHangOutWithNullInviteHangOutId() throws Exception {

        RejectInvitedHangOutRequest rejectInvitedHangOutRequest = new RejectInvitedHangOutRequest(null);

        mockMvc.perform(MockMvcRequestBuilders.delete("/hangOut/rejectInvitedHangOut")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(rejectInvitedHangOutRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}














