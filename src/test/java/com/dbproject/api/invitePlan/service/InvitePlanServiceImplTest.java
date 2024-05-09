package com.dbproject.api.invitePlan.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRouteRequest;
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
import com.dbproject.api.route.Route;
import com.dbproject.api.route.RouteRepository;
import com.dbproject.api.routeLocation.RouteLocation;
import com.dbproject.api.routeLocation.repository.RouteLocationRepository;
import com.dbproject.constant.InvitePlanStatus;
import com.dbproject.constant.PlanPeriod;
import com.dbproject.constant.RouteStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


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

    @Autowired
    private RouteLocationRepository routeLocationRepository;

    @Autowired
    private RouteRepository routeRepository;


    @BeforeEach
    void createMember() {

        Member member = memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));

        favoriteRepository.save(FavoriteLocation.of(member, locationRepository.findByLocationId("C1_379000000A_001572"), "메모 1 입니다."));
        favoriteRepository.save(FavoriteLocation.of(member, locationRepository.findByLocationId("C1_379000000A_000217"), "메모 1 입니다."));
        favoriteRepository.save(FavoriteLocation.of(member, locationRepository.findByLocationId("C1_379000000A_001591"), "메모 1 입니다."));
    }

    @DisplayName("make a plan and invite friend to join the plan")
    @Test
    void invitePlan(){
        //given
        List<String> friendEmailList = Arrays.asList("zxcv@zxcv.com", "yunni@yunni.com");
        InvitePlanRequest request = getRequestFrom("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", LocalDate.of(2024, 3, 20), 3, friendEmailList);

        //when
        Long invitePlanId = invitePlanService.invitePlan(request,"asdf@asdf.com");

        //then
        InvitePlan invitePlan = invitePlanRepository.findById(invitePlanId).get();
        assertInvitePlan(invitePlan);
    }


    private static void assertInvitePlan(InvitePlan invitePlan) {
        assertThat(invitePlan.getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(invitePlan.getSupply()).isEqualTo("hair dryer, slipper, brush");
        assertThat(invitePlan.getDepartDate()).isEqualTo("2024-03-20");
        assertThat(invitePlan.getArriveDate()).isEqualTo("2024-03-22");
        assertThat(invitePlan.getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(invitePlan.getRequester().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(invitePlan.getMembers().size()).isEqualTo(2);
        assertThat(invitePlan.getMembers().get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
//        assertThat(invitePlan.getInviteFriendList().get(0).getSupply()).isEqualTo("computer");
        assertThat(invitePlan.getMembers().get(0).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);
        assertThat(invitePlan.getMembers().get(1).getMember().getEmail()).isEqualTo("yunni@yunni.com");
//        assertThat(invitePlan.getInviteFriendList().get(1).getSupply()).isEqualTo("ipad");
        assertThat(invitePlan.getMembers().get(1).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);

        assertThat(invitePlan.getRoutes().get(0).getDay()).isEqualTo(1);
        assertThat(invitePlan.getRoutes().get(0).getRouteLocationList().size()).isEqualTo(3);
        assertThat(invitePlan.getRoutes().get(0).getRouteLocationList().get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(invitePlan.getRoutes().get(0).getRouteLocationList().get(1).getLocation().getName()).isEqualTo("台北101");
        assertThat(invitePlan.getRoutes().get(0).getRouteLocationList().get(2).getLocation().getName()).isEqualTo("台北地下街");
    }

    @DisplayName("get my invited plan list")
    @Test
    void getInvitedList(){
        //given
        saveInvitePlan("lee's 3 days tainan trip", PlanPeriod.LONGTRIP, "hair dryer, slipper, brush", 2024,3,20, 3, Arrays.asList("zxcv@zxcv.com", "yunni@yunni.com"), memberRepository.findByEmail("asdf@asdf.com"));
        saveInvitePlan("lee's 7 days japan trip", PlanPeriod.LONGTRIP, "computer, ipad", 2024,6,20, 7, Arrays.asList("zxcv@zxcv.com", "yunni@yunni.com"), memberRepository.findByEmail("asdf@asdf.com"));

        //when
        InvitedPlanListResponse response = invitePlanService.getInvitedList("yunni@yunni.com");

        //then
        assertGetInvitedList(response);
    }

    private static void assertGetInvitedList(InvitedPlanListResponse response) {
        assertThat(response.getInvitePlanDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(0).getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(response.getInvitePlanDtoList().get(0).getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(response.getInvitePlanDtoList().get(0).getDepartDate()).isEqualTo(LocalDate.of(2024,3,20));
        assertThat(response.getInvitePlanDtoList().get(0).getArriveDate()).isEqualTo(LocalDate.of(2024,3,22));
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(response.getInvitePlanDtoList().get(0).getInvitePlanMemberDtoList().get(1).getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(response.getInvitePlanDtoList().get(0).getRequesterEmail()).isEqualTo("asdf@asdf.com");
        assertThat(response.getInvitePlanDtoList().get(0).getRequesterName()).isEqualTo("이병민");
        assertThat(response.getInvitePlanDtoList().get(0).getRouteDtoList().get(0).getLocationDtos().get(0).getName()).isEqualTo("西門町");
        assertThat(response.getInvitePlanDtoList().get(0).getRouteDtoList().get(0).getLocationDtos().get(1).getName()).isEqualTo("台北101");
        assertThat(response.getInvitePlanDtoList().get(0).getRouteDtoList().get(0).getLocationDtos().get(2).getName()).isEqualTo("台北地下街");
        assertThat(response.getInvitePlanDtoList().get(1).getName()).isEqualTo("lee's 7 days japan trip");
        assertThat(response.getInvitePlanDtoList().get(1).getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(response.getInvitePlanDtoList().get(1).getDepartDate()).isEqualTo(LocalDate.of(2024,6,20));
        assertThat(response.getInvitePlanDtoList().get(1).getArriveDate()).isEqualTo(LocalDate.of(2024,6,26));
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().size()).isEqualTo(2);
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(response.getInvitePlanDtoList().get(1).getInvitePlanMemberDtoList().get(1).getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(response.getInvitePlanDtoList().get(1).getRequesterEmail()).isEqualTo("asdf@asdf.com");
        assertThat(response.getInvitePlanDtoList().get(1).getRequesterName()).isEqualTo("이병민");
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getRouteStatus()).isEqualTo(RouteStatus.INVITEPLAN);
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getDay()).isEqualTo(1);
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getLocationDtos().size()).isEqualTo(3);
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getLocationDtos().get(0).getName()).isEqualTo("西門町");
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getLocationDtos().get(1).getName()).isEqualTo("台北101");
        assertThat(response.getInvitePlanDtoList().get(1).getRouteDtoList().get(0).getLocationDtos().get(2).getName()).isEqualTo("台北地下街");
    }

    public InvitePlanRequest getRequestFrom(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, Integer tripDay, List<String> friendEmailList) {
        //        1. 여기 함수들을 request나 다른 클래스로 빼내려니  repository 를 찾는 코드를 어떻게 해야할지

        InvitePlanRequest invitePlanRequest = createRequest(
                name,
                planPeriod,
                supply,
                departDate,
                tripDay);

        setMemberRequestListTo(invitePlanRequest,friendEmailList);
        setRouteRequestListTo(invitePlanRequest);

        return invitePlanRequest;
    }


    public void setMemberRequestListTo(InvitePlanRequest request, List<String> friendEmailList){

        List<InvitePlanMemberRequest> memberRequestList = new ArrayList<>();

        for (String friendEmail : friendEmailList) {
            InvitePlanMemberRequest memberRequest = new InvitePlanMemberRequest(friendEmail);
            memberRequestList.add(memberRequest);
        }
        request.setMemberList(memberRequestList);
    }

    public void setRouteRequestListTo(InvitePlanRequest request) {

        List<InvitePlanRouteRequest> routeRequestList = new ArrayList<>();

        InvitePlanRouteRequest routeRequest = new InvitePlanRouteRequest(1);
        routeRequest.setLocationRequestList(getInvitePlanLocationRequestList());

        routeRequestList.add(routeRequest);
        request.setRouteList(routeRequestList);
    }

    private List<InvitePlanLocationRequest> getInvitePlanLocationRequestList() {

        List<InvitePlanLocationRequest> locationRequests = new ArrayList<>();

        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findByMemberEmail("asdf@asdf.com");
        for (FavoriteLocation favoriteLocation : favoriteLocationList) {

            InvitePlanLocationRequest locationRequest = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocation.getId()));
            locationRequests.add(locationRequest);
        }

        return locationRequests;
    }


    public void setMemberListTo(InvitePlan savedInvitePlan, InvitePlanRequest request){

        List<InvitePlanMember> members = new ArrayList<>();

        for (InvitePlanMemberRequest memberRequest: request.getMemberList()) {

            createAndSaveAndAddMemberFromRequest(savedInvitePlan, members, memberRequest);
        }
        savedInvitePlan.setMembers(members);
    }

    private void createAndSaveAndAddMemberFromRequest(InvitePlan savedInvitePlan, List<InvitePlanMember> members, InvitePlanMemberRequest memberRequest) {
        //0.Member 찾기
        Member member = memberRepository.findByEmail(memberRequest.getFriendEmail());
        //1. InvitePlanMember 생성 및 저장
        InvitePlanMember invitePlanMember = createAndSaveInvitePlanMember(savedInvitePlan, member);
        //2. InvitePlanMemberList 에 InvitePlanMember  추가
        members.add(invitePlanMember);
    }

    private InvitePlanMember createAndSaveInvitePlanMember(InvitePlan savedInvitePlan, Member member) {
        InvitePlanMember invitePlanMember = InvitePlanMember.createWithoutSupply(member, savedInvitePlan);
        invitePlanMemberRepository.save(invitePlanMember);
        return invitePlanMember;
    }

// 테스트 코드에서 많은 클래스의 메서드 중복을 처리하는 법 ?

    public void setRouteListTo(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Route> routeList = new ArrayList<>();

//        2일 이상의 여행에서는 루트도 2개 이상
        for (InvitePlanRouteRequest routeRequest : request.getRouteList()) {

            Route route = Route.createRoute(routeRequest);
//            route 를 저장해서 id 가 있는 route 를 넘기기
            Route savedRoute = routeRepository.save(route);


            savedRoute.setRouteLocationList(getRouteLocationList(routeRequest,savedRoute));
            routeList.add(savedRoute);

        }

        savedInvitePlan.setRoutes(routeList);
    }

    private List<RouteLocation> getRouteLocationList(InvitePlanRouteRequest routeRequest, Route route) {

        List<RouteLocation> routeLocationList = new ArrayList<>();
        for (InvitePlanLocationRequest locationRequest : routeRequest.getLocationRequestList()) {

            Location location = getLocationFromRequest(locationRequest);

            RouteLocation routeLocation = RouteLocation.createRouteLocation(route, location);
            RouteLocation savedRouteLocation = routeLocationRepository.save(routeLocation);


            routeLocationList.add(savedRouteLocation);
        }

        return routeLocationList;
    }


    private Location getLocationFromRequest(InvitePlanLocationRequest locationRequest) {
        Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
        FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
        Location location = favoriteLocation.getLocation();
        return location;
    }


    public void saveInvitePlan(String name, PlanPeriod planPeriod, String supply, int year, int month, int day, int tripDay, List<String> friendEmailList,Member requester) {

        InvitePlanRequest request = getRequestFrom(name,
                planPeriod,
                supply,
                LocalDate.of(year, month, day),
                tripDay,
                friendEmailList);


        saveInvitePlanFrom(request,requester);
    }

    private void saveInvitePlanFrom(InvitePlanRequest request, Member requester) {

        InvitePlan savedInvitePlan = invitePlanRepository.save(InvitePlan.createInvitePlan(request, requester));
        setMemberListTo(savedInvitePlan, request);
        setRouteListTo(savedInvitePlan, request);
    }

    public InvitePlanRequest createRequest(String name, PlanPeriod planPeriod, String supply ,LocalDate departDate, Integer tripDay) {

        return InvitePlanRequest.of(
                name,
                planPeriod,
                supply,
                departDate,
                tripDay);
    }



}