package com.dbproject.api.friend.friendRequest;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> ,
        QuerydslPredicateExecutor<FavoriteLocation>,
        FriendRequestRepositoryCustom {


//    checkDuplicate
//    email 로 찾는게 더 빠를까 ?
//    쉽게 생각하면 email 로 찾는게 더 빠를것 같긴 한데 내부에서 한번더 쿼리가 나가는건가 ?
    FriendRequest findByRequesterAndRespondent(Member requester,Member respondent);

    @Query("select fr from FriendRequest fr" +
            " where fr.requester.email = :requesterEmail" +
            " and fr.respondent.email = :respondentEmail")
    FriendRequest findByRequesterEmailAndRespondentEmail(@Param("requesterEmail") String requesterEmail, @Param("respondentEmail") String respondentEmail);

    @Modifying
    @Query("delete from FriendRequest fr" +
            " where fr.requester.email = :email" +
            " or fr.respondent.email = :email")
    void deleteByEmail(@Param("email") String email);


}

