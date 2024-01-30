package com.dbproject.api.member.memberImg;

import com.dbproject.api.File.FileService;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.RegisterFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberImgServiceTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberImgRepository memberImgRepository;

    @Autowired
    private MemberImgService memberImgService;

    @MockBean
    private FileService fileService;


    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("이병민");
        registerFormDto.setAddress("강원 원주시");
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }


    @DisplayName("회원 이미지를 저장합니다")
    @Test
    void  updateMemberImg() throws Exception {


        //given
        String oriImgName = "oriImg.jpg";
        String imgName = "imgName.jpg";
        String imgUrl = "/images/member/" + imgName;
        Member member = memberRepository.findByEmail("asdf@asdf.com");


        //mockMultifile 과 when thenReturn 으로 만들기
//        대안으로는 uuid 만드는 부분을 여기서 만들어서 파라미터로 줄 수 있게 ->

        MockMultipartFile multipartFile = new MockMultipartFile(imgName, oriImgName, "text/plain", oriImgName.getBytes(StandardCharsets.UTF_8) );
        when(fileService.uploadFile(imgUrl, oriImgName, oriImgName.getBytes(StandardCharsets.UTF_8))).thenReturn(imgName);

        //when
        memberImgService.updateMemberImg(multipartFile, member.getEmail());

        //then
        Optional<MemberImg> optionalMemberImg = memberImgRepository.findByMemberEmail("asdf@asdf.com");
        MemberImg savedMemberImg = optionalMemberImg .get();

        assertThat(savedMemberImg.getImgUrl()).isEqualTo(imgUrl);
     }

}