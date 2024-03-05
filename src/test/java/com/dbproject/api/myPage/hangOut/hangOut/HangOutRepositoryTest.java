package com.dbproject.api.myPage.hangOut.hangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.hangOut.hangOut.HangOut;
import com.dbproject.api.hangOut.hangOut.repository.HangOutRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.hangOut.inviteHangOut.repository.InviteHangOutRepository;
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
@TestPropertySource(locations = "classpath:application-test.properties")
class HangOutRepositoryTest {


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

    @Autowired
    private HangOutRepository hangOutRepository;


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


    @DisplayName("HangOut 을 저장합니다")
    @Test
    void save(){
        //given
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        Member lee = memberRepository.findByEmail("qwer@qwer.com");
        String message = "메시지 입니다.";
        LocalDateTime localDateTime = LocalDateTime.now();

        HangOut hangOut = new HangOut(location, yunni, lee, message, localDateTime);

        //when
        hangOutRepository.save(hangOut);

        //then
        List<HangOut> hangOutList = hangOutRepository.findAll();
        assertThat(hangOutList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(hangOutList.get(0).getRequester().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(hangOutList.get(0).getRespondent().getEmail()).isEqualTo("qwer@qwer.com");
    }


    @DisplayName("회원 email 로 회원의 Email 이 포함된 hangout을 모두 삭제합니다")
    @Test
    void deleteByEmail(){

        //given
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        Member lee = memberRepository.findByEmail("qwer@qwer.com");
        String message = "메시지 입니다.";
        LocalDateTime localDateTime = LocalDateTime.now();
        HangOut hangOut = new HangOut(location, yunni, lee, message, localDateTime);
        hangOutRepository.save(hangOut);
        List<HangOut> savedHangOutList = hangOutRepository.findAll();
        assertThat(savedHangOutList).hasSize(1);

        //when
        hangOutRepository.deleteByEmail("qwer@qwer.com");

        //then
        List<HangOut> hangOutList = hangOutRepository.findAll();
        assertThat(hangOutList).hasSize(0);

    }
}