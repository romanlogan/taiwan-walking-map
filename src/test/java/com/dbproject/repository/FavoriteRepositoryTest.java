package com.dbproject.repository;

import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.RegisterFormDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//    testProperty (h2)사용시 h2 에는 location 에 대한 데이터가 없으므로 테스트가 작동 x
@TestPropertySource(locations="classpath:application-test.properties")
class FavoriteRepositoryTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;


    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("zxcv@zxcv.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("가고싶은 장소를 즐겨찾기 리스트에 추가 한다")
    @Test
    void test(){
        //given
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        /// 여기 locaiton 을 못찾음
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";

        FavoriteLocation favoriteLocation = new FavoriteLocation(member, location, memo);

        //when
        favoriteRepository.save(favoriteLocation);

        //then
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findAll();
        assertThat(favoriteLocationList).hasSize(1);
        assertThat(favoriteLocationList.get(0).getLocation().getName()).isEqualTo("西門町");
     }

     @DisplayName("locationId 로 이미 즐겨찾기 리스트에 저장된 장소인지 확인한다")
     @Test
     void findByLocationId(){
         //given
         Member member = memberRepository.findByEmail("zxcv@zxcv.com");
         String locationId = "C1_379000000A_001572";
         Location location = locationRepository.findByLocationId(locationId);
         String memo = "메모 1 입니다.";
         FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
         favoriteRepository.save(savedFavoriteLocation);


         //when
         FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(locationId);

         //then
         assertThat(favoriteLocation.getLocation().getName()).isEqualTo("西門町");
      }

    @DisplayName("email 로 즐겨찾기 리스트를 가져온다")
    @Test
    void getFavoriteLocationList(){

        //given
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);

        //when
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.getFavoriteLocationList("zxcv@zxcv.com");

        //then
        assertThat(favoriteLocationList.get(0).getLocation().getName()).isEqualTo("西門町");
    }





}