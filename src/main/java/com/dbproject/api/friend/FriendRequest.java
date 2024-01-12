package com.dbproject.api.friend;

import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "friend_request")
@Getter
@Setter
public class FriendRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_request_id")
    private Long id;

    private String memo;

    //어떻게 매핑은 email 로 되지만 Member 객체 타입인거지 ?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_email",
            referencedColumnName = "member_email")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_email",
            referencedColumnName = "member_email")
    private Member respondent;


    public FriendRequest() {
    }

    @Builder
    public FriendRequest(String memo, Member requester, Member respondent) {
        this.memo = memo;
        this.requester = requester;
        this.respondent = respondent;
    }

    public static FriendRequest createFriendRequest(Member requester, Member respondent,String memo) {

        return FriendRequest.builder()
                .memo(memo)
                .requester(requester)
                .respondent(respondent)
                .build();

    }

}
