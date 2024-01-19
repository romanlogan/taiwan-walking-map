package com.dbproject.api.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FriendRepository extends JpaRepository<Friend, Long> ,
        QuerydslPredicateExecutor<Friend>,
        FriendRepositoryCustom{

}
