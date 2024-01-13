package com.dbproject.api.friend;


import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.member.Member;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.constant.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "friend")
@Getter
@Setter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    //어떻게 매핑은 email 로 되지만 Member 객체 타입인거지 ?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_email",
            referencedColumnName = "member_email")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_email",
            referencedColumnName = "member_email")
    private Member respondent;

    public Friend() {
    }

    @Builder
    public Friend(Member requester, Member respondent) {
        this.requester = requester;
        this.respondent = respondent;
    }

    public static Friend createFriend(Member requester, Member respondent) {

        return Friend.builder()
                .requester(requester)
                .respondent(respondent)
                .build();

    }


}















