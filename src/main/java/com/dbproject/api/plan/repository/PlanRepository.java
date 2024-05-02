package com.dbproject.api.plan.repository;

import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {


    Plan findByInvitePlan(InvitePlan invitePlan);
}
