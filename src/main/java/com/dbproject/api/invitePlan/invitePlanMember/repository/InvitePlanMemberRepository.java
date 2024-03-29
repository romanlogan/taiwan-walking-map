package com.dbproject.api.invitePlan.invitePlanMember.repository;

import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitePlanMemberRepository extends JpaRepository<InvitePlanMember, Long> {


//    초대받은 Plan 목록을 가져온다 (내가 만든 Plan 은 없음)
    @Query("select ipm from InvitePlanMember ipm" +
            " where ipm.member.email = :email" +
            " and ipm.invitePlanStatus = com.dbproject.constant.InvitePlanStatus.WAITING")
    List<InvitePlanMember> getInvitedPlanMemberListByEmail(String email);


    @Query("select ipm from InvitePlanMember ipm" +
            " where ipm.invitePlan.id = :id" +
            " and ipm.member.email = :email")
    InvitePlanMember getByIdAndEmail(Long id, String email);

}
