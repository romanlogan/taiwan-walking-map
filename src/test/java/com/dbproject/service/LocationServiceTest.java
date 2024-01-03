package com.dbproject.service;

import com.dbproject.dto.LocationDtlResponse;
import com.dbproject.dto.RegisterFormDto;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.entity.Location;
import com.dbproject.entity.Member;
import com.dbproject.repository.FavoriteRepository;
import com.dbproject.repository.LocationRepository;
import com.dbproject.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LocationServiceTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

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

    @DisplayName("비로그인 유저가 추천 장소의 Id 를 주면 그 추천 장소를 가져온다")
    @Test
    void getLocationDtl(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtl(locationId);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
     }


    @DisplayName("로그인 유저가 즐겨찾기 리스트에 추가된 장소인지 확인하고 true 를 저장한다")
    @Test
    void checkSavedFavoriteLocationTrue(){
        //given
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location);
        favoriteRepository.save(savedFavoriteLocation);


        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,member.getEmail());

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(true);
    }


    @DisplayName("로그인 유저가 즐겨찾기 리스트에 추가된 장소인지 확인하고 false 를 저장한다")
    @Test
    void checkSavedFavoriteLocationFalse(){

        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtl(locationId);

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(false);
    }

     @DisplayName("추천 도시 이름과 pageable 로 추천 도시의 추천 장소 리스트를 페이징하여 돌려준다")
     @Test
     void getLocationPageByCity(){

         //given
         //0번째 페이지
         Pageable pageable = PageRequest.of(0, 5);
         String city = "臺北市";

         //when
         Page<Location> locationListPage = locationRepository.getLocationPageByCity(city, pageable);
         List<Location> pageContent = locationListPage.getContent();

         //then
         assertThat(locationListPage.getTotalPages()).isEqualTo(90);
         assertThat(locationListPage.getTotalElements()).isEqualTo(449);
         assertThat(pageContent).hasSize(5);

     }
}