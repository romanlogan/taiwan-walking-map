package com.dbproject.api.friend;


import com.dbproject.entity.QMember;
import com.dbproject.web.friend.RequestFriendListDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {


    private JPAQueryFactory queryFactory;

    public  FriendRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }


//    @Override
//    public Page<FriendListResponse> getFriendListPage(Pageable pageable, String email) {
//
//        QMember member = QMember.member;
//        QFriend friend = QFriend.friend;
//
//
//        //select
//        //from member m, friend f
//        //where f.requester_email = email OR f.respondent_email = email
//        //order by
//        List<FriendListResponse> content = queryFactory
//                .select(
//                        new QFriendListResponse(
//                                friend.id,
//                                friend.requester.email,
//                                friend.respondent.email,
////                              내가 가지고 오려는 친구 목록이 내가 신청한 친구 일수도 있고 내가 신청 받은 친구 일 수 도 있어서
////                                어떤 email 을 가지고 와야 하나 ,만약 respondent.email 을 가져오면 내가 신청 받은 친구 상황은 내 이메일 이 반환 될텐데
////                                친구 테이블 설계를 잘못한건가
////                                일대다 양방향으로 member 가 friend 목록을 가지고 있게 설게해야하나 ?
////                                그러면 친구 요청을 수락했을대 member
//
//
//
//
//                        )
//                )
//                .from(friend)
//                .where(friend.requester.email.eq(email)||friend.respondent.email.eq(email))
//    }
}
