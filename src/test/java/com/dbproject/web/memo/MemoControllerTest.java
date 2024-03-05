package com.dbproject.web.memo;

import com.dbproject.api.member.MemberService;
import com.dbproject.api.memo.MemoService;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
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


@WebMvcTest(controllers = MemoController.class)
class MemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemoService memoService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("즐겨찾기 장소 id 와 메모를 받아서 메모를 수정 한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateMemo() throws Exception{
        //given

        Integer favoriteLocationId = 1;
        String memo = "memo 1";

        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(favoriteLocationId, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/memo/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("비 로그인 유저가 메모 수정에 접근시 UNAUTHORIZED 를 반환")
    @Test
    void updateMemoWithoutLogin() throws Exception{
        //given

        Integer favoriteLocationId = 1;
        String memo = "memo 1";

        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(favoriteLocationId, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/memo/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }





    @DisplayName("즐겨찾기 장소 id 가 null 이면 BadRequest 를 반환")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void favoriteLocationIdCanNotBlank2() throws Exception{
        //given

        Integer favoriteLocationId = null;
        String memo = "memo 1";

        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(favoriteLocationId, memo);

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.put("/memo/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}