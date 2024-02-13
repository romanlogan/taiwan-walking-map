package com.dbproject.api.myPage.hangOut.hangOut;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HangOutRepository extends JpaRepository<HangOut, Long> {

    @Query("select ho from HangOut ho"+
            " where ho.respondent.email = :email"+
            " order by ho.departDateTime asc")
    List<HangOut> getHangOutListByRespondentEmail(@Param("email") String email);
}
