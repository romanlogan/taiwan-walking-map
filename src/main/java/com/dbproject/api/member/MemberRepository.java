package com.dbproject.api.member;


import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    @Query("select m from Member m" +
            " where m.email = :email")
    Optional<Member> findOptionalMemberByEmail(String email);

    void deleteByEmail(String email);
}
