package com.dbproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("get Location's detail page when login user ")
    public void getLocationDetailPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/location/C1_379000000A_000243"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("get Location's detail page when non-logged-in user")
    public void getLocationDetailPageTestWithoutLogin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/location/C1_379000000A_000243"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get a list of recommended locations in the city.")
    public void getRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendLocationList/0")
                        .param("searchArrival","臺北市")
                        .param("searchQuery","公園")
                        .param("searchTown","中山區")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When Get a list of recommended locations in the city, searchQuery can be empty string")
    public void searchQueryCanBeEmptyStringWhenGetRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendLocationList/0")
                        .param("searchArrival","臺北市")
                        .param("searchQuery","")
                        .param("searchTown","中山區")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When Get a list of recommended locations in the city, searchTown can be empty string")
    public void searchTownCanBeEmptyStringWhenGetRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendLocationList/0")
                        .param("searchArrival","臺北市")
                        .param("searchQuery","公園")
                        .param("searchTown","")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When Get a list of recommended locations in the city, searchTown cannot be null")
    public void searchTownCannotBeNullWhenGetRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendLocationList/0")
                        .param("searchArrival", "臺北市")
                        .param("searchQuery", "公園")
                        .param("searchTown", "asdf")
                )
                .andDo(print())
                .andExpect(status().isOk());
                //  Town 검색어가 올바르지 않습니다. 메시지를 반환
    }

//    .param() 에 null 을 보내는 방법은 ?

//    @Test
//    @DisplayName("When Get a list of recommended locations in the city, searchQuery cannot be null")
//    public void searchQueryCannotBeNullWhenGetRecommendLocationPageTest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/recommendLocationList/0")
//                        .param("searchArrival", "臺北市")
//                        .param("searchQuery", )
//                        .param("searchTown", "中山區")
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

//    @Test
//    @DisplayName("When Get a list of recommended locations in the city, searchArrival cannot be null")
//    public void searchArrivalCannotBeNullWhenGetRecommendLocationPageTest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/recommendLocationList/0")
//                        .param("searchArrival", String.valueOf(nullValue()))
//                        .param("searchQuery", "公園")
//                        .param("searchTown", "中山區")
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @Test
    @DisplayName("When Get a list of recommended locations in the city, searchArrival cannot be blank")
    public void searchArrivalCannotBeBlankWhenGetRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/recommendLocationList/0")
                        .param("searchArrival", "")
                        .param("searchQuery", "公園")
                        .param("searchTown", "中山區")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }



}