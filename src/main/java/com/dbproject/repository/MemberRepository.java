package com.dbproject.repository;


import com.dbproject.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    void deleteByEmail(String email);
}
