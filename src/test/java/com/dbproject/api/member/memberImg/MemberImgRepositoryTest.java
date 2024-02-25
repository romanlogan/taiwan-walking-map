package com.dbproject.api.member.memberImg;

import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.RegisterFormDto;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberImgRepositoryTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberImgRepository memberImgRepository;

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

    @DisplayName("회원의 이미지 파일을 저장합니다.")
    @Test
    void save(){

        //given
        String oriImgName = "oriImg.jpg";
        String imgName = "imgName.jpg";
        String imgUrl = "/images/member/" + imgName;
        Member member = memberRepository.findByEmail("asdf@asdf.com");

        MemberImg memberImg = new MemberImg(imgName, oriImgName, imgUrl, member);

        //when
        MemberImg savedMemberImg = memberImgRepository.save(memberImg);

        //then
        assertThat(savedMemberImg.getMember().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(savedMemberImg.getMember().getName()).isEqualTo("이병민");
        assertThat(savedMemberImg.getImgName()).isEqualTo(imgName);
        assertThat(savedMemberImg.getOriImgName()).isEqualTo(oriImgName);
        assertThat(savedMemberImg.getImgUrl()).isEqualTo(imgUrl);
    }

    @DisplayName("회원의 email 로 회원의 이미지를 삭제합니다")
    @Test
    void deleteByMemberEmail(){
        //given
        String oriImgName = "oriImg.jpg";
        String imgName = "imgName.jpg";
        String imgUrl = "/images/member/" + imgName;
        Member member = memberRepository.findByEmail("asdf@asdf.com");
        MemberImg memberImg = new MemberImg(imgName, oriImgName, imgUrl, member);
        memberImgRepository.save(memberImg);
        Member savedMember = memberRepository.findByEmail("asdf@asdf.com");
        assertThat(savedMember).isNotNull();

        //when
        memberRepository.deleteByEmail("asdf@asdf.com");

        //then
        Member deletedMember = memberRepository.findByEmail("asdf@asdf.com");
        assertThat(deletedMember).isNotNull();

    }




}