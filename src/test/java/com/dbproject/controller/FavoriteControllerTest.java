package com.dbproject.controller;

import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.favorite.service.FavoriteServiceImpl;
import com.dbproject.api.member.MemberService;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.web.favorite.FavoriteController;
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

import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FavoriteController.class)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private FavoriteServiceImpl favoriteService;

    @MockBean
    private FavoriteRepository favoriteRepository;


    @DisplayName("즐겨찾기 장소를 등록한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addFavoriteList() throws Exception {

        //given
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("비 로그인 유저가 즐겨찾기 장소를 등록시 Unauthorized 를 반환한다")
    @Test
    void addFavoriteListWithoutLogin() throws Exception {

        //given
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("즐겨찾기 장소를 등록시 location Id는 null 이 될 수 없다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void LocationIdCanNotNullWhenAddFavoriteList() throws Exception {

        //given
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(null, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messageList",Matchers.hasItems("locationId值是必要")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(nullValue())));
    }

    @DisplayName("儲存FavoriteLocation時locationId值只能20字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveFavoriteLocationWithNotFormattedLocationId() throws Exception {

        //given
        //19
        String locationId = "abcdeabcdeabcdeabcd";
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messageList",Matchers.hasItems("locationId要20個字")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("abcdeabcdeabcdeabcd")));
    }

    @DisplayName("儲存FavoriteLocation時memo值最多只能255字")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveFavoriteLocationWithOverSizeMemo() throws Exception {

        //given
        String locationId = "C1_379000000A_001572";
//        256 字
        String memo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messageList",Matchers.hasItems("memo值只能最多255字")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid")));
    }



    @DisplayName("즐겨찾기 장소들을 조회한다")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFavoriteList() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 5 );

        FavoriteLocationList favoriteListResponse = new FavoriteLocationList("C1_379000000A_001572", "西門町");
        List<FavoriteLocationList> list = new ArrayList<>();
        list.add(favoriteListResponse);

        Page<FavoriteLocationList> page = new PageImpl<>(list, pageable, 1);
        Mockito.when(favoriteService.getFavoriteLocationList(pageable, "qwer@qwer.com")).thenReturn(page);


//      현재 문제는 MockUser를 사용하여 test 진행시 mockUser가 faovriteList가 없는 문제/
//        QueryProjection 의 테스트 방법이 따로 있나?
//        결과가 계속 null 이 들어가서 html 에서 에러 발생

//        stubbing 으로 객체를 stub 객체를 반환하여서 해결해야함
        //when  //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/favorite/favoriteList/0")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("如果未登入使用者查詢FavoriteLocationList時，return 401 UnAuthorization Error")
    @Test
//    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFavoriteListWithNoLoginUser() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 5 );

        FavoriteLocationList favoriteListResponse = new FavoriteLocationList("C1_379000000A_001572", "西門町");
        List<FavoriteLocationList> list = new ArrayList<>();
        list.add(favoriteListResponse);

        Page<FavoriteLocationList> page = new PageImpl<>(list, pageable, 1);
        Mockito.when(favoriteService.getFavoriteLocationList(pageable, "qwer@qwer.com")).thenReturn(page);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/favorite/favoriteList/0")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @DisplayName("즐겨찾기 장소를 삭제한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteFavoriteLocation() throws Exception {

        //given
        Integer favoriteLocationId = 1;

        DeleteFavoriteLocationRequest deleteFavoriteLocationRequest = new DeleteFavoriteLocationRequest(favoriteLocationId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/deleteFavorite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("비 로그인 유저가 즐겨찾기 장소를 삭제시  Unauthorized 를 반환한다 ")
    @Test
    void deleteFavoriteLocationWithoutLogin() throws Exception {

        //given
        Integer favoriteLocationId = 1;

        DeleteFavoriteLocationRequest deleteFavoriteLocationRequest = new DeleteFavoriteLocationRequest(favoriteLocationId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/deleteFavorite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @DisplayName("즐겨찾기 장소를 삭제시 favoriteLocationId 는 null 이 될 수 없다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void favoriteLocationIdCanNotNullWhenDeleteFavoriteLocation() throws Exception {

        //given
        Integer favoriteLocationId = null;

        DeleteFavoriteLocationRequest deleteFavoriteLocationRequest = new DeleteFavoriteLocationRequest(favoriteLocationId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/deleteFavorite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("刪除FavoriteLocation時，favoriteLocationId不能0")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteFavoriteLocationWithZeroFavoriteLocationId() throws Exception {

        //given
        Integer favoriteLocationId = 0;

        DeleteFavoriteLocationRequest deleteFavoriteLocationRequest = new DeleteFavoriteLocationRequest(favoriteLocationId);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/favorite/deleteFavorite")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(deleteFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("favoriteLocationId不能低於1")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(0)));
    }

    @DisplayName("등록된 즐겨찾기 장소의 메모를 변경한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateMemo() throws Exception {

        //given
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(1, "memo1");

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
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
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.messageList", Matchers.hasItems("favoriteLocationId值是必要")))
                .andExpect(jsonPath("$.dataList", Matchers.hasItems(nullValue()))) ;
    }

}