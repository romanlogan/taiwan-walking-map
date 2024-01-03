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

@SpringBootTest
@AutoConfigureMockMvc
class LocationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Location 상세 정보 페이지 연결 테스트")
    public void getLocationDetailPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/location/C1_379000000A_000243"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("추천 Location 페이지 연결 테스트")
    public void getRecommendLocationPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/recommendLocationList/0?searchArrival=臺北市"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}