package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.constant.PlanPeriod;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

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

    private List<InvitePlanMemberRequest> invitePlanMemberRequestList = new ArrayList<>();

    private List<InvitePlanRouteRequest> invitePlanRouteRequestList = new ArrayList<>();

    public InvitePlanRequest() {
    }

    public InvitePlanRequest(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, LocalDate arriveDate) {
        this.name = name;
        this.planPeriod = planPeriod;
        this.supply = supply;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
    }

    @Override
    public String toString() {
        return "InvitePlanRequest{" +
                "name='" + name + '\'' +
                ", planPeriod=" + planPeriod +
                ", supply='" + supply + '\'' +
                ", departDate=" + departDate +
                ", arriveDate=" + arriveDate +
                ",\n invitePlanMemberRequestList=" + invitePlanMemberRequestList.get(0).getFriendEmail() +
                '}';
    }
}
