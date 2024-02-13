package com.dbproject.api.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long>,
        QuerydslPredicateExecutor<Friend>,
        FriendRepositoryCustom {


//    이건 어떨까 ? schedule 에서 시험해보기
//    @Query("select f from Friend f" +
//            " where f.me.email = :email"+
//            " or f.newFriend = : email")

    @Query("select f from Friend f"+
            " where f.me.email = :email"+
            " order by f.newFriend.name asc")
    List<Friend> getFriendList(@Param("email") String email);


}
