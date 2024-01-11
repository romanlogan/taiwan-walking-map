package com.dbproject.api.friend;


import com.dbproject.dto.QFavoriteListResponse;
import com.dbproject.entity.QMember;
import com.dbproject.web.favorite.FavoriteListResponse;
import com.dbproject.web.friend.QRequestFriendListDto;
import com.dbproject.web.friend.RequestFriendListDto;
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
                                friendRequest.requester.email,
                                friendRequest.requester.name
                        )
                )
                .from(friendRequest)
//                .join(friendRequest.respondent, member)
                .where(friendRequest.respondent.email.eq(email))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(friendRequest.id.desc())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(friendRequest)
//                .join(friendRequest.respondent, member)
                .where(friendRequest.respondent.email.eq(email))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }





}
