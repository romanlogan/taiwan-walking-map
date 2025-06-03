package com.dbproject.api.friend.friendRequest;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.member.Member;
import com.dbproject.constant.FriendRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
@Getter
@Setter
public class FriendRequest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id")
    private Long id;

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendRequestStatus friendRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_email", referencedColumnName = "member_email")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_email", referencedColumnName = "member_email")
    private Member respondent;


    public FriendRequest() {
    }

    @Builder
    public FriendRequest(String memo, FriendRequestStatus friendRequestStatus, Member requester, Member respondent) {
        this.memo = memo;
        this.friendRequestStatus = friendRequestStatus;
        this.requester = requester;
        this.respondent = respondent;
    }


    public static FriendRequest createFriendRequest(Member requester, Member respondent, String memo) {

        return FriendRequest.builder()
                .memo(memo)
                .friendRequestStatus(FriendRequestStatus.WAITING)
                .requester(requester)
                .respondent(respondent)
                .build();
    }

    public void changeStatus(FriendRequestStatus status) {
        this.friendRequestStatus = status;
    }

}
