package com.dbproject.api.invitePlan.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitedPlanListResponse;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.invitePlanMember.repository.InvitePlanMemberRepository;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.constant.InvitePlanStatus;
import com.dbproject.constant.PlanPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.PushBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class InvitePlanServiceImplTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InvitePlanService invitePlanService;

    @Autowired
    private InvitePlanRepository invitePlanRepository;

    @Autowired
    private InvitePlanMemberRepository invitePlanMemberRepository;



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
        registerFormDto2.setEmail("asdf@asdf.com");
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

    public void setInvitePlanMemberRequestList(InvitePlanRequest request, List<String> friendEmailList){

        List<InvitePlanMemberRequest> invitePlanMemberRequestList = new ArrayList<>();
        for (String friendEmail : friendEmailList) {
            InvitePlanMemberRequest invitePlanMemberRequest = new InvitePlanMemberRequest(friendEmail);
            invitePlanMemberRequestList.add(invitePlanMemberRequest);
        }
        request.setInvitePlanMemberRequestList(invitePlanMemberRequestList);
    }

    public void setInvitePlanLocationRequestList(InvitePlanRequest request) {

        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findByMemberEmail("asdf@asdf.com");
        List<InvitePlanLocationRequest> invitePlanLocationRequestList = new ArrayList<>();

        for (FavoriteLocation favoriteLocation : favoriteLocationList) {

            InvitePlanLocationRequest invitePlanLocationRequest = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocation.getId()));
            invitePlanLocationRequestList.add(invitePlanLocationRequest);
        }

        request.setInvitePlanLocationRequestList(invitePlanLocationRequestList);
    }


    public void setInvitePlanMemberList(InvitePlan savedInvitePlan, InvitePlanRequest request){
        List<InvitePlanMember> invitePlanMemberList = new ArrayList<>();
        for (InvitePlanMemberRequest memberRequest: request.getInvitePlanMemberRequestList()) {
            //0.Member 찾기
            String email = memberRequest.getFriendEmail();
            Member member = memberRepository.findByEmail(email);

            //1. InvitePlanMember 생성 및 저장
            InvitePlanMember invitePlanMember = InvitePlanMember.createInvitePlanMemberWithoutSupply(member, savedInvitePlan);
            invitePlanMemberRepository.save(invitePlanMember);

            //2. InvitePlanMemberList 에 InvitePlanMember  추가
            invitePlanMemberList.add(invitePlanMember);
        }
        savedInvitePlan.setInviteFriendList(invitePlanMemberList);
    }



    public void setLocationList(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Location> locationList = new ArrayList<>();
        for (InvitePlanLocationRequest locationRequest : request.getInvitePlanLocationRequestList()) {
            //            fetch 조인으로 바꿀 필요
            Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
            FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
            Location location = favoriteLocation.getLocation();

//            Location location = locationRepository.findByLocationId();       //savedInvitePlan 이 id 가 0인데 이 아이디가 0인 savedInvitePlan 을 찾아르 수 없다
            locationList.add(location);
        }
        savedInvitePlan.setLocationList(locationList);
    }


//    public InvitePlanRequest createRequest() {
//        String name = "lee's 3 days tainan trip";
//        PlanPeriod planPeriod = PlanPeriod.LONGTRIP;
//        String supply = "hair dryer, slipper, brush";
//        LocalDate departDate = LocalDate.of(2024, 3,20);
//        LocalDate arriveDate = LocalDate.of(2024, 3,22);
//        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);
//
//        return request;
//    }

    public InvitePlanRequest createRequest(String name, PlanPeriod planPeriod, String supply ,LocalDate departDate, Integer tripDay) {

        LocalDate arriveDate = LocalDate.of(departDate.getYear(), departDate.getMonthValue(), departDate.getDayOfMonth() + tripDay - 1);
        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);

        return request;
    }

    public void saveInvitePlan(String name, PlanPeriod planPeriod, String supply, int year, int month, int day, int tripDay, List<String> friendEmailList) {

        LocalDate departDate = LocalDate.of(year, month,day);

        InvitePlanRequest request = createRequest(name,
                planPeriod,
                supply,
                departDate,
                tripDay);
        setInvitePlanMemberRequestList(request,friendEmailList);
        setInvitePlanLocationRequestList(request);

        InvitePlan invitePlan = InvitePlan.createInvitePlan(request);
        InvitePlan saveInvitePlan = invitePlanRepository.save(invitePlan);
        setInvitePlanMemberList(saveInvitePlan,request);
        setLocationList(saveInvitePlan,request);
    }

    @DisplayName("製作plan，邀請朋友參加plan")
    @Test
    void invitePlan(){
        //given
        List<String> friendEmailList = new ArrayList<>();
        friendEmailList.add("zxcv@zxcv.com");
        friendEmailList.add("yunni@yunni.com");

        LocalDate departDate = LocalDate.of(2024, 3,20);
        InvitePlanRequest request = createRequest("lee's 3 days tainan trip",
                PlanPeriod.LONGTRIP,
                "hair dryer, slipper, brush",
                departDate,
                3);
        setInvitePlanMemberRequestList(request,friendEmailList);
        setInvitePlanLocationRequestList(request);

        //when
        Long invitePlanId = invitePlanService.invitePlan(request);

        //then
        Optional<InvitePlan> savedInvitePlan = invitePlanRepository.findById(invitePlanId);
        InvitePlan invitePlan = savedInvitePlan.get();
        assertThat(invitePlan.getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(invitePlan.getSupply()).isEqualTo("hair dryer, slipper, brush");
        assertThat(invitePlan.getDepartDate()).isEqualTo("2024-03-20");
        assertThat(invitePlan.getArriveDate()).isEqualTo("2024-03-22");
        assertThat(invitePlan.getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(invitePlan.getInviteFriendList().size()).isEqualTo(2);
        assertThat(invitePlan.getInviteFriendList().get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
//        assertThat(invitePlan.getInviteFriendList().get(0).getSupply()).isEqualTo("computer");
        assertThat(invitePlan.getInviteFriendList().get(0).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);
        assertThat(invitePlan.getInviteFriendList().get(1).getMember().getEmail()).isEqualTo("yunni@yunni.com");
//        assertThat(invitePlan.getInviteFriendList().get(1).getSupply()).isEqualTo("ipad");
        assertThat(invitePlan.getInviteFriendList().get(1).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);

        assertThat(invitePlan.getLocationList().size()).isEqualTo(3);
        assertThat(invitePlan.getLocationList().get(0).getName()).isEqualTo("西門町");
        assertThat(invitePlan.getLocationList().get(1).getName()).isEqualTo("台北101");
        assertThat(invitePlan.getLocationList().get(2).getName()).isEqualTo("台北地下街");
    }

    @DisplayName("return受到邀請的PlanList")
    @Test
    void getInvitedList(){
        //given
        List<String> friendEmailList = new ArrayList<>();
        friendEmailList.add("zxcv@zxcv.com");
        friendEmailList.add("yunni@yunni.com");

        List<String> friendEmailList2 = new ArrayList<>();
        friendEmailList2.add("zxcv@zxcv.com");
        friendEmailList2.add("yunni@yunni.com");

        // create Request
        saveInvitePlan("lee's 3 days tainan trip",
                PlanPeriod.LONGTRIP,
                "hair dryer, slipper, brush",
                2024,3,20, 3,
                friendEmailList);

        saveInvitePlan("lee's 7 days japan trip",
                PlanPeriod.LONGTRIP,
                "computer, ipad",
                2024,6,20, 7,
                friendEmailList2);

        //when
        InvitedPlanListResponse response = invitePlanService.getInvitedList("yunni@yunni.com");

        //then
        assertThat(response.getInvitePlanDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(0).getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(response.getInvitePlanDtoList().get(0).getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(response.getInvitePlanDtoList().get(0).getDepartDate()).isEqualTo(LocalDate.of(2024,3,20));
        assertThat(response.getInvitePlanDtoList().get(0).getArriveDate()).isEqualTo(LocalDate.of(2024,3,22));
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().get(1).getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(response.getInvitePlanDtoList().get(1).getName()).isEqualTo("lee's 7 days japan trip");
        assertThat(response.getInvitePlanDtoList().get(1).getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(response.getInvitePlanDtoList().get(1).getDepartDate()).isEqualTo(LocalDate.of(2024,6,20));
        assertThat(response.getInvitePlanDtoList().get(1).getArriveDate()).isEqualTo(LocalDate.of(2024,6,26));
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().get(1).getEmail()).isEqualTo("yunni@yunni.com");

     }



}