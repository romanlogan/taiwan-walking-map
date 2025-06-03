package com.dbproject.web.myPage;

import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.api.member.memberImg.MemberImgRepository;
import com.dbproject.api.member.memberImg.MemberImgServiceImpl;
import com.dbproject.api.member.dto.UpdateProfileRequest;
import com.dbproject.api.hangOut.inviteHangOut.service.InviteHangOutServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MyPageController.class)
class MyPageControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MemberImgRepository memberImgRepository;

    @MockBean
    private MemberImgServiceImpl memberImgService;

    @MockBean
    private InviteHangOutServiceImpl inviteHangOutService;

    @DisplayName("회원의 회원 정보를 가져옵니다.")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getMemberProfile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/myPage/profile")
                )
                .andDo(print())
                .andExpect(status().isOk());
     }

    @DisplayName("회원 정보를 업데이트 합니다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateProfile() throws Exception {

        String name = "LeeByeongMin";
        String address = "danguro";
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(name, address);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/myPage/update")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateProfileRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}