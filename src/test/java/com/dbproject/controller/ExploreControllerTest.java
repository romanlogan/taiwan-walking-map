package com.dbproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
class ExploreControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("도시 페이지 연결 테스트")
    public void getCityPage() throws Exception{

        mockMvc.perform(
                    get("/exploreCity")
                            .queryParam("searchArrival", "臺北市")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("도시 이름은 필수 값입니다.")
    @Test
    void test() throws Exception {

        //given

        //when
        //then

//        입구 getMapping 은 일반형으로 검사하고 나머지는 rest 로 검사하기

//        mockMvc.perform(
//                        get("/exploreCity")
//                                .queryParam("searchArrival", "")
//                )
//                .andDo(print())
//                .andExpect(status().isBadRequest());

     }

//    @Test
//    @DisplayName("상품 등록 페이지 권한 테스트")
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    public void itemFormTest() throws Exception{
//        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

}