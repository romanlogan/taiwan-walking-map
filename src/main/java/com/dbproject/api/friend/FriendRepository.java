package com.dbproject.api.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long>,
        QuerydslPredicateExecutor<Friend>,
        FriendRepositoryCustom {


    @Query("select f from Friend f"+
            " where f.me.email = :email"+
            " order by f.newFriend.name asc")
    List<Friend> getFriendList(@Param("email") String email);


    @Modifying
    @Query("delete from Friend f"+
            " where f.me.email = :email"+
            " or f.newFriend.email = :email")
    void deleteByEmail(@Param("email") String email);


    //친구 삭제
}
