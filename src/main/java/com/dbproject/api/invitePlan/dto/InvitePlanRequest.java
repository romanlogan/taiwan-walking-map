package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.constant.PlanPeriod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitePlanRequest {

    // NotBlank 를 써야할까 ?
    @NotBlank(message = "name值是必要")     //null "" " " "  "
    @Length(min = 1, max = 50, message = "name值是必要1到50個字")  // "     " 가 입력되면 NotBlank 에서 처리되어 name值是必要 메시지가 출력된다
    private String name;

    @NotNull(message = "planPeriod值是必要")
    private PlanPeriod planPeriod;

    @Length(max = 1000, message = "supply值只能最多1000個字")  //여기서 Blank 체크 까지 해주는듯
    private String supply;

    @NotNull(message = "departDate值是必要")
//    @JsonFormat(pattern = "YYYY-MM-DD")           //jsonFormat 쓰고 3월 20로 보내니 body 에 3월 60일로 전송 ?
    private LocalDate departDate;

//    @JsonFormat(pattern = "YYYY-MM-DD")
    private LocalDate arriveDate;

//    private String requesterEmail;

    private List<InvitePlanMemberRequest> memberList = new ArrayList<>();

    private List<InvitePlanRouteRequest> routeList = new ArrayList<>();

    public InvitePlanRequest() {
    }

    @Builder
    public InvitePlanRequest(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, LocalDate arriveDate) {
        this.name = name;
        this.planPeriod = planPeriod;
        this.supply = supply;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
    }

    public static InvitePlanRequest of(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, Integer tripDay) {

        LocalDate arriveDate = LocalDate.of(departDate.getYear(), departDate.getMonthValue(), departDate.getDayOfMonth() + tripDay - 1);

        return InvitePlanRequest.builder()
                .name(name)
                .planPeriod(planPeriod)
                .supply(supply)
                .departDate(departDate)
                .arriveDate(arriveDate)
                .build();
    }

    @Override
    public String toString() {
        return "InvitePlanRequest{" +
                "name='" + name + '\'' +
                ", planPeriod=" + planPeriod +
                ", supply='" + supply + '\'' +
                ", departDate=" + departDate +
                ", arriveDate=" + arriveDate +
                ",\n invitePlanMemberRequestList=" + memberList.get(0).getFriendEmail() +
                '}';
    }

//    public static InvitePlanRequest createForTest(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, Integer tripDay, List<String> friendEmailList) {
//
//        InvitePlanRequest invitePlanRequest = InvitePlanRequest.of(
//                name,
//                planPeriod,
//                supply,
//                departDate,
//                3);
//
//
//        invitePlanRequest.setMemberListForTest(invitePlanRequest,friendEmailList);
//        invitePlanRequest.setRouteListForTest(invitePlanRequest);
//    }
//
//    public void setMemberListForTest(InvitePlanRequest invitePlanRequest, List<String> friendEmailList){
//
//        List<InvitePlanMemberRequest> requestList = new ArrayList<>();
//
//        for (String friendEmail : friendEmailList) {
//            InvitePlanMemberRequest request = new InvitePlanMemberRequest(friendEmail);
//            requestList.add(request);
//        }
//        invitePlanRequest.setMemberList(requestList);
//    }
//
//    public void setRouteListForTest(InvitePlanRequest invitePlanRequest) {
//
//        List<InvitePlanRouteRequest> routeRequestList = new ArrayList<>();
//
//        InvitePlanRouteRequest routeRequest = new InvitePlanRouteRequest(1);
//        routeRequest.setLocationRequestList(getInvitePlanLocationRequestList());
//
//        routeRequestList.add(routeRequest);
//        invitePlanRequest.setRouteList(routeRequestList);
//    }
//
//    private List<InvitePlanLocationRequest> getInvitePlanLocationRequestList() {
//
//        List<InvitePlanLocationRequest> invitePlanLocationRequestList = new ArrayList<>();

//    여기 리포지토리를 찾는 부분이 문제
//        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findByMemberEmail("asdf@asdf.com");
//
//        for (FavoriteLocation favoriteLocation : favoriteLocationList) {
//
//            InvitePlanLocationRequest invitePlanLocationRequest = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocation.getId()));
//            invitePlanLocationRequestList.add(invitePlanLocationRequest);
//        }
//
//        return invitePlanLocationRequestList;
//    }

}
