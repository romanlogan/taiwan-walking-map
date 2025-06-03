package com.dbproject.api.friend.repository;


import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.friend.QFriend;
//import com.dbproject.api.friend.QFriendListResponse;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public  FriendRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

//    @Override
//    public Page<FriendDto> getFriendListPage(Pageable pageable, String email) {
//
//        QFriend friend = QFriend.friend;
//
//        //select
//        //from member m, friend f
//        //where f.requester_email = email OR f.respondent_email = email
//        //order by
//        List<FriendDto> content = queryFactory
//                .select(
//                        new QFriendListResponse(
//                                friend.id,
//                                friend.newFriend.email,
//                                friend.newFriend.name,
//                                friend.newFriend.address
//                        )
//                )
//                .from(friend)
//                .where(friend.me.email.eq(email))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .orderBy(friend.newFriend.name.asc())
//                .fetch();
//
//
//        Long total = queryFactory
//                .select(Wildcard.count)
//                .from(friend)
//                .where(friend.me.email.eq(email))
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total);
//    }
}
