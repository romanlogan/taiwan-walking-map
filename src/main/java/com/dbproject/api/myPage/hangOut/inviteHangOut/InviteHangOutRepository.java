package com.dbproject.api.myPage.hangOut.inviteHangOut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InviteHangOutRepository extends JpaRepository<InviteHangOut, Long> {

    //여기 성능 장애 의심됨 fetch join 으로 바꾸기
    @Query("select iho from InviteHangOut iho" +
            " where iho.respondent.email = :requesterEmail and iho.inviteHangOutStatus = com.dbproject.constant.InviteHangOutStatus.WAITING" +
            " order by iho.regTime asc")
    List<InviteHangOut> findWaitingListByRequesterEmail(@Param("requesterEmail") String email);



    @Modifying
    @Query("delete from InviteHangOut iho" +
            " where iho.requester.email = :email" +
            " or iho.respondent.email = :email")
    void deleteByEmail(@Param("email") String email);


}
