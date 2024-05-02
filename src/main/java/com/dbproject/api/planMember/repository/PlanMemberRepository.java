package com.dbproject.api.planMember.repository;

import com.dbproject.api.planMember.PlanMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanMemberRepository extends JpaRepository<PlanMember, Long> {
}
