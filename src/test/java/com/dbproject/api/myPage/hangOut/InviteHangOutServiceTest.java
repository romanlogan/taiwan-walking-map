package com.dbproject.api.myPage.hangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.RegisterFormDto;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRepository;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRequest;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutService;
import com.dbproject.constant.InviteHangOutStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class InviteHangOutServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InviteHangOutRepository InviteHangOutRepository;

    @Autowired
    private InviteHangOutService InviteHangOutService;



    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto1 = new RegisterFormDto();
        registerFormDto1.setName("손흥민");
        registerFormDto1.setAddress("서울 강남구");
        registerFormDto1.setEmail("zxcv@zxcv.com");
        registerFormDto1.setPassword("1234");

        Member member1 = Member.createMember(registerFormDto1, passwordEncoder);
        memberRepository.save(member1);

        RegisterFormDto registerFormDto2 = new RegisterFormDto();
        registerFormDto2.setName("이병민");
        registerFormDto2.setAddress("강원도 원주시");
        registerFormDto2.setEmail("qwer@qwer.com");
        registerFormDto2.setPassword("1234");

        Member member2 = Member.createMember(registerFormDto2, passwordEncoder);
        memberRepository.save(member2);

        RegisterFormDto registerFormDto3 = new RegisterFormDto();
        registerFormDto3.setName("장원유");
        registerFormDto3.setAddress("대만 산총구");
        registerFormDto3.setEmail("yunni@yunni.com");
        registerFormDto3.setPassword("1234");

        Member member3 = Member.createMember(registerFormDto3, passwordEncoder);
        memberRepository.save(member3);

        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";
        FavoriteLocation favoriteLocation = new FavoriteLocation(member2, location, memo);
        favoriteRepository.save(favoriteLocation);

    }

    @DisplayName("InviteHangOutRequest 으로 친구를 HangOut 에 초대합니다")
    @Test
    void inviteHangOut() {

        //given
        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId("C1_379000000A_001572");
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@yunni.com";
        String myEmail = "qwer@qwer.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocation.getId(), departDateTime, message, friendEmail);

        //when
        Long id = InviteHangOutService.inviteHangOut(inviteHangOutRequest, myEmail);

        //then
        List<InviteHangOut> InviteHangOutList = InviteHangOutRepository.findAll();
        assertThat(InviteHangOutList).size().isEqualTo(1);
        assertThat(InviteHangOutList.get(0).getMessage()).isEqualTo("message 1");
        assertThat(InviteHangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(InviteHangOutList.get(0).getInviteHangOutStatus()).isEqualTo(InviteHangOutStatus.WAITING);
        assertThat(InviteHangOutList.get(0).getRequester().getEmail()).isEqualTo("qwer@qwer.com");
        assertThat(InviteHangOutList.get(0).getRespondent().getEmail()).isEqualTo("yunni@yunni.com");

    }
}