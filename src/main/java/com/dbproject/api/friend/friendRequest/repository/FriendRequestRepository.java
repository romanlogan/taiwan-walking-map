package com.dbproject.api.friend.friendRequest.repository;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> ,
        QuerydslPredicateExecutor<FavoriteLocation>,
        FriendRequestRepositoryCustom {

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

