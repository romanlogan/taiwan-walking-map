package com.dbproject.api.invitePlan.repository;

import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitePlanRepository extends JpaRepository<InvitePlan, Long> {


    List<InvitePlan> findByRequester(Member requester);

}
