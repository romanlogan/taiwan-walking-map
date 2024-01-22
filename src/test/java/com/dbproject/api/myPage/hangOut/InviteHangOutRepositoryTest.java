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
class InviteHangOutRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InviteHangOutRepository inviteHangOutRepository;



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

        String ximending = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(ximending);
        String memo = "메모 1 입니다.";
        FavoriteLocation favoriteLocation = new FavoriteLocation(member2, location, memo);
        favoriteRepository.save(favoriteLocation);

        String taipei101 = "C1_379000000A_000217";
        Location location2 = locationRepository.findByLocationId(taipei101);
        String memo2 = "메모 1 입니다.";
        FavoriteLocation favoriteLocation2 = new FavoriteLocation(member2, location2, memo2);
        favoriteRepository.save(favoriteLocation2);

        String taipeidixiajie = "C1_379000000A_001591";
        Location location3 = locationRepository.findByLocationId(taipeidixiajie);
        String memo3 = "메모 1 입니다.";
        FavoriteLocation favoriteLocation3 = new FavoriteLocation(member2, location3, memo3);
        favoriteRepository.save(favoriteLocation3);

    }

    @DisplayName("InviteHangOut 을 저장합니다")
    @Test
    void saveHangOut(){

        //given
        LocalDateTime departDateTime = LocalDateTime.now();

        InviteHangOut inviteHangOut = getInviteHangOut(departDateTime, "C1_379000000A_001572", "qwer@qwer.com",InviteHangOutStatus.WAITING);

        //when
        inviteHangOutRepository.save(inviteHangOut);

        //then
        List<InviteHangOut> InviteHangOutList = inviteHangOutRepository.findAll();
        assertThat(InviteHangOutList).size().isEqualTo(1);
        assertThat(InviteHangOutList.get(0).getMessage()).isEqualTo("message 1");
        assertThat(InviteHangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(InviteHangOutList.get(0).getInviteHangOutStatus()).isEqualTo(InviteHangOutStatus.WAITING);
        assertThat(InviteHangOutList.get(0).getRequester().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(InviteHangOutList.get(0).getRespondent().getEmail()).isEqualTo("qwer@qwer.com");
     }

    private InviteHangOut getInviteHangOut(LocalDateTime departDateTime, String locationId, String friendEmail,InviteHangOutStatus inviteHangOutStatus) {
        String message = "message 1";
        Member me = memberRepository.findByEmail("yunni@yunni.com");
        Member friend = memberRepository.findByEmail(friendEmail);

        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(locationId);


        return InviteHangOut.createHangOut(message, departDateTime, favoriteLocation, me, friend, inviteHangOutStatus);
    }


    @DisplayName("나의 email 로 받은 약속 초대 리스트를 날짜 순으로 가져옵니다")
     @Test
     void findByRequesterEmail(){
         //given
        LocalDateTime departDateTime = LocalDateTime.now();

        InviteHangOut inviteHangOut = getInviteHangOut(departDateTime, "C1_379000000A_001572", "qwer@qwer.com",InviteHangOutStatus.WAITING);
        InviteHangOut inviteHangOut2 = getInviteHangOut(departDateTime.plusDays(1), "C1_379000000A_000217", "qwer@qwer.com",InviteHangOutStatus.WAITING);
        inviteHangOutRepository.save(inviteHangOut);
        inviteHangOutRepository.save(inviteHangOut2);

        InviteHangOut inviteHangOut3 = getInviteHangOut(departDateTime.plusDays(2), "C1_379000000A_000217", "qwer@qwer.com",InviteHangOutStatus.ACCEPTED);
        inviteHangOutRepository.save(inviteHangOut3);


        //when
        List<InviteHangOut> myInvitedHangOutList = inviteHangOutRepository.findWaitingListByRequesterEmail("qwer@qwer.com");

         //then
        assertThat(myInvitedHangOutList).hasSize(2);
        assertThat(myInvitedHangOutList.get(0).getFavoriteLocation().getLocation().getName()).isEqualTo("西門町");
        assertThat(myInvitedHangOutList.get(1).getFavoriteLocation().getLocation().getName()).isEqualTo("台北101");


      }

}