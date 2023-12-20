package com.dbproject.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuickSearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("QuickSearch 페이지 연결 테스트")
    public void getQuickSearchResultPageTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/quickSearch/0"))
                .andDo(print())
                .andExpect(status().isOk());

    }


}