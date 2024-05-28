package com.dbproject.controller;

import com.dbproject.api.favorite.dto.*;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.favorite.service.FavoriteServiceImpl;
import com.dbproject.api.member.MemberService;
import com.dbproject.web.favorite.FavoriteController;
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


    @DisplayName("add Favorite Location in member's favoriteLocationList")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void addFavoriteList() throws Exception {
        //given
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest("C1_379000000A_001572", "메모 1 입니다.");

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("When Non-logged-in members access to add favorite location, return 401 unAuthorized")
    @Test
    void addFavoriteListWithoutLogin() throws Exception {
        //given
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest("C1_379000000A_001572", "메모 1 입니다.");

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.post("/favorite/addFavoriteList")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(addFavoriteLocationRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("when add favorite location, location Id can't be null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void LocationIdCanNotNullWhenAddFavoriteList() throws Exception {
        //given
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(null, "메모 1 입니다.");

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
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId value is required"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value(nullValue()));
    }

    @DisplayName("location id only can 20 word, can't shorter than 20, when add FavoriteLocation")
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
                .andExpect(jsonPath("$.errorMap.locationId.message").value("locationId requires only 20 characters"))
                .andExpect(jsonPath("$.errorMap.locationId.rejectedValue").value("abcdeabcdeabcdeabcd"));
    }

    @DisplayName("The maximum number of memo characters is 255, when save FavoriteLocation")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void saveFavoriteLocationWithOverSizeMemo() throws Exception {

        //given
        //        256 字
        String memo = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid";

        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest("C1_379000000A_001572", memo);

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
                .andExpect(jsonPath("$.errorMap.memo.message").value("The memo value can be up to 255 characters long."))
                .andExpect(jsonPath("$.errorMap.memo.rejectedValue").value("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Massa tincidunt dui ut ornare. Ipsum dolor sit amet consectetur adipiscing elit. Dolor sit amet consectetur adipiscing. Massa tincid"));
    }



    @DisplayName("get member's favorite location list")
    @Test
    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFavoriteLocationList() throws Exception {

        //given
        Pageable pageable = PageRequest.of(0, 5 );
        FavoriteLocationListResponse response = getFavoriteLocationListResponse(pageable);

        Mockito.when(favoriteService.getFavoriteLocationList(pageable, "qwer@qwer.com")).thenReturn(response);


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

    private static FavoriteLocationListResponse getFavoriteLocationListResponse(Pageable pageable) {

        List<FavoriteLocationDto> favoriteLocationDtos = new ArrayList<>();

        FavoriteLocationDto favoriteLocationDto = new FavoriteLocationDto(1L,"hi","C1_379000000A_001572", "西門町",null,null,null,null,null);

        favoriteLocationDtos.add(favoriteLocationDto);
        Page<FavoriteLocationDto> page = new PageImpl<>(favoriteLocationDtos, pageable, 1);
        return new FavoriteLocationListResponse(page);
    }


    @DisplayName("When Non-logged-in members access to get favorite location list, return 401 unAuthorized")
    @Test
//    @WithMockUser(username = "qwer@qwer.com", roles = "USER")
    void getFavoriteListWithNoLoginUser() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        FavoriteLocationListResponse response = getFavoriteLocationListResponse(pageable);

        Mockito.when(favoriteService.getFavoriteLocationList(pageable, "qwer@qwer.com")).thenReturn(response);

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/favorite/favoriteList/0")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("delete favorite location")
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

    @DisplayName("When Non-logged-in members access to delete favorite location, return 401 unAuthorized")
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



    @DisplayName("When delete FavoriteLocation, favoriteLocationId can't be null")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId value is required"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(nullValue()));
    }

    @DisplayName("When delete FavoriteLocation, favoriteLocationId can't be 0")
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
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId can't be lower than 1"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(0));
    }

    @DisplayName("When delete FavoriteLocation, favoriteLocationId can't be negative")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void deleteFavoriteLocationWithNegativeFavoriteLocationId() throws Exception {

        //given
        Integer favoriteLocationId = -1;

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
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId can't be lower than 1"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(-1));
    }


    @DisplayName("update the memo of a saved favoriteLocation")
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

    @DisplayName("When Non-logged-in members access to update memo of favorite location, return 401 unAuthorized")
    @Test
    void updateMemoWithoutLogin() throws Exception{
        //given
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(1, "memo 1");

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("When update memo, favorite location id can't be null")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void favoriteLocationIdCanNotNullWhenUpdateMemo() throws Exception{
        //given
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(null, "memo 1");

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
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId value is required"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(nullValue()));
    }

    @DisplayName("When update memo of FavoriteLocation, favoriteLocationId can't be 0")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateMemoOfFavoriteLocationWithZeroFavoriteLocationId() throws Exception {

        //given
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(0, "memo 1");

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId can't be lower than 1"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(0));
    }

    @DisplayName("When update memo of FavoriteLocation, favoriteLocationId can't be negative")
    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateMemoOfFavoriteLocationWithNegativeFavoriteLocationId() throws Exception {
        //given
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(-1, "memo 1");

        //when  //then
        mockMvc.perform(MockMvcRequestBuilders.put("/favorite/updateMemo")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(updateMemoRequest))
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.message").value("favoriteLocationId can't be lower than 1"))
                .andExpect(jsonPath("$.errorMap.favoriteLocationId.rejectedValue").value(-1));
    }


}