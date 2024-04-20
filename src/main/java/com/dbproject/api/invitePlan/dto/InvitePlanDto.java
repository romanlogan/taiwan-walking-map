package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.route.RouteDto;
import com.dbproject.constant.PlanPeriod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitePlanDto {

    private Long id;
    private String name;
    private PlanPeriod period;
    private String supply;
    private LocalDate departDate;
    private LocalDate arriveDate;
    private String requesterEmail;
    private String requesterName;



//    List<FriendDto> friendDtoList = new ArrayList<>();
    List<InvitePlanMemberDto> invitePlanMemberDtoList = new ArrayList<>();

    // 내가 초대 받은 InvitePlan 의 목록을 보여주는것이므로 favoriteLocation 보다는 Location 사용

    List<RouteDto> routeDtoList = new ArrayList<>();

//    List<LocationDto> locationDtoList = new ArrayList<>();

    public InvitePlanDto() {
    }

    @Builder
    public InvitePlanDto(Long id,String name, PlanPeriod period, String supply, LocalDate departDate, LocalDate arriveDate,String requesterEmail, String requesterName) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.supply = supply;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
    }

    public static InvitePlanDto of(InvitePlan invitePlan) {

        return InvitePlanDto.builder()
                .id(invitePlan.getId())
                .name(invitePlan.getName())
                .period(invitePlan.getPeriod())
                .supply(invitePlan.getSupply())
                .departDate(invitePlan.getDepartDate())
                .arriveDate(invitePlan.getArriveDate())
                .requesterEmail(invitePlan.getRequester().getEmail())
                .requesterName(invitePlan.getRequester().getName())
                .build();
    }
}
