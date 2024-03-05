package com.dbproject.controller;

import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberControllerTest {


    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    public void createMember(String email, String password){


        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail(email);
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울시 마포구 합정동");
        registerFormDto.setPassword(password);
        registerFormDto.setPhoneNumber("0912345678");
        registerFormDto.setGender(1);
        registerFormDto.setDateOfBirth(LocalDate.now());
        registerFormDto.setAcceptReceiveAdvertising(true);

        memberService.saveMember(registerFormDto,passwordEncoder);
    }

    @Test
    @DisplayName("formLogin() 방식 로그인 성공 테스트")
    public void loginSuccessTestByFormLogin() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password(password))
                .andDo(print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email, password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password("12345"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

    @Test
    @DisplayName("MockMvcRequestBuilders 방식 로그인 성공 테스트")
    public void loginSuccessTestByMockMvcRequestBuilders() throws Exception{
//        String email = "test@email.com";
//        String password = "1234";
//        this.createMember(email, password);
//

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("test@email.com");
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울시 마포구 합정동");
        registerFormDto.setPassword("12341234");
        registerFormDto.setPhoneNumber("0912345678");
        registerFormDto.setGender(1);
        registerFormDto.setDateOfBirth(LocalDate.now());
        registerFormDto.setAcceptReceiveAdvertising(true);


        mockMvc.perform(MockMvcRequestBuilders.post("/members/registration")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(registerFormDto))
                )
                .andDo(print())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("登入時name field不能空白")
    public void loginSuccessTest2() throws Exception{
//        String email = "test@email.com";
//        String password = "1234";
//        this.createMember(email, password);
//

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("test@email.com");
        registerFormDto.setName(" ");
        registerFormDto.setAddress("서울시 마포구 합정동");
        registerFormDto.setPassword("12341234");
        registerFormDto.setPhoneNumber("0912345678");
        registerFormDto.setGender(1);
        registerFormDto.setDateOfBirth(LocalDate.now());
        registerFormDto.setAcceptReceiveAdvertising(true);


        mockMvc.perform(MockMvcRequestBuilders.post("/members/registration")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(registerFormDto))
                )
                .andDo(print())
                //다시 회원가입창으로 돌아가기 때문에 여전히 status 는 200 인데 어떻게 검증하지 ?
                .andExpect(status().isBadRequest());
    }
}