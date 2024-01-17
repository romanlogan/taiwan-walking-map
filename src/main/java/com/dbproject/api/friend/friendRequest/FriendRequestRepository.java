package com.dbproject.api.friend.friendRequest;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> ,
        QuerydslPredicateExecutor<FavoriteLocation>,
        FriendRequestRepositoryCustom {


    FriendRequest findByRequesterAndRespondent(Member requester, Member respondent);



}

