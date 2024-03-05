package com.dbproject.service;

import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.service.FavoriteServiceImpl;
import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.member.Member;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FavoriteServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("test@test.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("用locationId和email來，Location儲存在FavoriteList")
    @Test
    void addFavoriteList(){
        //given
        String email = "test@test.com";
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when
        favoriteService.addFavoriteList(addFavoriteLocationRequest, email);

        //then
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findAll();
        assertThat(favoriteLocationList).hasSize(1);
        assertThat(favoriteLocationList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(favoriteLocationList.get(0).getMemo()).isEqualTo("메모 1 입니다.");
     }

    @DisplayName("儲存在FavoriteList時，如果已儲存過的地方，就return DuplicateFavoriteLocationException")
    @Test
    void addFavoriteListWithAddDuplicateFavoriteLocation(){
        //given

        String email = "test@test.com";
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        addFavoriteLocation(email, locationId);

        //when //then
        assertThatThrownBy(() -> favoriteService.addFavoriteList(addFavoriteLocationRequest, email))
                .isInstanceOf(DuplicateFavoriteLocationException.class)
                .hasMessage("이미 등록된 장소 입니다.");
    }

    private Long addFavoriteLocation(String email, String locationId) {
        String memo = "메모 1 입니다.";
        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(member, location, memo);
        favoriteRepository.save(favoriteLocation);
        return favoriteLocation.getId();
    }


    @DisplayName("儲存6個FavoriteLocation後，getFavoriteLocationListPage時，第一頁有5個FavoriteLocation")
    @Test
    void getFirstPageInFavoriteLocationListPageAfterAddSixFavoriteLocation(){
        //given
        String email = "test@test.com";
        addFavoriteLocation(email,"C1_379000000A_001571");
        addFavoriteLocation(email,"C1_379000000A_001572");
        addFavoriteLocation(email,"C1_379000000A_001573");
        addFavoriteLocation(email,"C1_379000000A_001574");
        addFavoriteLocation(email,"C1_379000000A_001575");
//        1576 not exist
        addFavoriteLocation(email,"C1_379000000A_001577");

        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<FavoriteLocationList> favoriteLocationPage = favoriteService.getFavoriteLocationList(pageable, "test@test.com");

        //then
        assertThat(favoriteLocationPage.getTotalElements()).isEqualTo(6);
        assertThat(favoriteLocationPage.getTotalPages()).isEqualTo(2);
        assertThat(favoriteLocationPage.getContent().size()).isEqualTo(5);
        assertThat(favoriteLocationPage.getContent().get(0).getLocationId()).isEqualTo("C1_379000000A_001571");
        assertThat(favoriteLocationPage.getContent().get(0).getName()).isEqualTo("台北花市");
        assertThat(favoriteLocationPage.getContent().get(4).getLocationId()).isEqualTo("C1_379000000A_001575");
        assertThat(favoriteLocationPage.getContent().get(4).getName()).isEqualTo("光華玉市");
    }

    @DisplayName("儲存6個FavoriteLocation後，getFavoriteLocationListPage時，第二頁只有1個FavoriteLocation")
    @Test
    void getSecondPageInFavoriteLocationListPageAfterAddSixFavoriteLocation(){
        //given
        String email = "test@test.com";
        addFavoriteLocation(email,"C1_379000000A_001571");
        addFavoriteLocation(email,"C1_379000000A_001572");
        addFavoriteLocation(email,"C1_379000000A_001573");
        addFavoriteLocation(email,"C1_379000000A_001574");
        addFavoriteLocation(email,"C1_379000000A_001575");
//        1576 not exist
        addFavoriteLocation(email,"C1_379000000A_001577");

        Pageable pageable = PageRequest.of(1, 5);

        //when
        Page<FavoriteLocationList> favoriteLocationPage = favoriteService.getFavoriteLocationList(pageable, "test@test.com");

        //then
        assertThat(favoriteLocationPage.getTotalElements()).isEqualTo(6);
        assertThat(favoriteLocationPage.getTotalPages()).isEqualTo(2);
        assertThat(favoriteLocationPage.getContent().size()).isEqualTo(1);
        assertThat(favoriteLocationPage.getContent().get(0).getLocationId()).isEqualTo("C1_379000000A_001577");
        assertThat(favoriteLocationPage.getContent().get(0).getName()).isEqualTo("建國假日花市");
     }


     @DisplayName("刪除FavoriteLocation")
     @Test
     void deleteFavoriteLocation(){
         //given
         String email = "test@test.com";
         addFavoriteLocation(email,"C1_379000000A_001571");
         addFavoriteLocation(email,"C1_379000000A_001572");
         List<FavoriteLocation> favoriteLocationList = favoriteRepository.findAll();
         assertThat(favoriteLocationList.size()).isEqualTo(2);

         FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId("C1_379000000A_001571");

         //when
         favoriteService.deleteFavoriteLocation(Math.toIntExact(favoriteLocation.getId()));

         //then
         List<FavoriteLocation> favoriteLocationListAfterDeleted = favoriteRepository.findAll();
         assertThat(favoriteLocationListAfterDeleted.size()).isEqualTo(1);
      }


    @DisplayName("更新FavoriteLocation的Memo")
    @Test
    void updateMemo() {
        //given
//        1. favoriteLocation 1개 저장
        String email = "test@test.com";
        String locationId = "C1_379000000A_001572";
        Long favoriteLocationId = addFavoriteLocation(email, locationId);

        //2. 저장한 favoriteLocation 의 memo 를 변경
        UpdateMemoRequest updateMemoRequest = new UpdateMemoRequest(Math.toIntExact(favoriteLocationId), "updated memo");

        //when
        favoriteService.updateMemo(updateMemoRequest);

        //then
        Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(favoriteLocationId);
        FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
        assertThat(favoriteLocation.getMemo()).isEqualTo("updated memo");
        assertThat(favoriteLocation.getLocation().getName()).isEqualTo("西門町");


    }
}