package com.dbproject.api.member.memberImg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {

    @Query("select mi from MemberImg mi"+
            " where mi.member.email =:email")
    Optional<MemberImg> findByMemberEmail(@Param("email") String email);

    @Modifying
    @Query("delete from MemberImg mi" +
            " where mi.member.email = :email")
    void deleteByMemberEmail(@Param("email") String email);


}
