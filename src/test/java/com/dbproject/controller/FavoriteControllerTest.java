package com.dbproject.controller;

import com.dbproject.api.favorite.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.favorite.FavoriteService;
import com.dbproject.api.member.MemberService;
import com.dbproject.web.favorite.FavoriteController;
import com.dbproject.api.favorite.FavoriteListResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteRepository favoriteRepository;


    @DisplayName("즐겨찾기 장소들을 조회한다")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFavoriteList() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 5 );

        FavoriteListResponse favoriteListResponse = new FavoriteListResponse("C1_379000000A_001572", "西門町");
        List<FavoriteListResponse> list = new ArrayList<>();
        list.add(favoriteListResponse);

        Page<FavoriteListResponse> page = new PageImpl<>(list, pageable, 1);
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
        String locationId = null;
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("즐겨찾기 장소를 등록시 location Id는 ' ' 이 될 수 없다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void LocationIdCanNotEmptyWhenAddFavoriteList() throws Exception {

        //given
        String locationId = " ";
        String memo = "메모 1 입니다.";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("즐겨찾기 장소를 삭제한다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteFavoriteLocation() throws Exception {

        //given
        String favoriteLocationId = "1";

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
        String favoriteLocationId = "1";

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
        String favoriteLocationId = null;

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

    @DisplayName("즐겨찾기 장소를 삭제시 favoriteLocationId 는 ' ' 이 될 수 없다")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void favoriteLocationIdCanNotBlankWhenDeleteFavoriteLocation() throws Exception {

        //given
        String favoriteLocationId = " ";

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


}