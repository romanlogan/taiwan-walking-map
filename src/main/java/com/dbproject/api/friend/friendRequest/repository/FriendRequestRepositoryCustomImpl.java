package com.dbproject.api.friend.friendRequest.repository;


import com.dbproject.api.friend.friendRequest.QFriendRequest;
import com.dbproject.api.friend.friendRequest.dto.QRequestFriendListDto;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.api.member.QMember;
import com.dbproject.constant.FriendRequestStatus;
//import com.dbproject.entity.QMember;
//import com.dbproject.web.friend.QRequestFriendListDto;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class FriendRequestRepositoryCustomImpl implements FriendRequestRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public FriendRequestRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<RequestFriendListDto> getRequestFriendListPage(Pageable pageable, String email) {

        QMember member = QMember.member;
        QFriendRequest friendRequest = QFriendRequest.friendRequest;

//        select m.name, m.email
//        from FriendRequest
//        join member on FriendRequest.register = email
        List<RequestFriendListDto> content = queryFactory
                .select(
                        new QRequestFriendListDto(
                                friendRequest.id,
                                friendRequest.requester.email,
                                friendRequest.requester.name,
                                friendRequest.friendRequestStatus
                        )
                )
                .from(friendRequest)
//                .join(friendRequest.respondent, member)
                .where(friendRequest.respondent.email.eq(email),
                        friendRequest.friendRequestStatus.eq(FriendRequestStatus.WAITING))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(friendRequest.id.desc())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(friendRequest)
//                .join(friendRequest.respondent, member)
                .where(friendRequest.respondent.email.eq(email),
                        friendRequest.friendRequestStatus.eq(FriendRequestStatus.WAITING))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }





}
