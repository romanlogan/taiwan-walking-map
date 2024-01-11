package com.dbproject.api.member;


import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    void deleteByEmail(String email);
}
