package com.dbproject.api.friend;


import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "friend")
@Getter
@Setter
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_email",
            referencedColumnName = "member_email")
    private Member me;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_email",
            referencedColumnName = "member_email")
    private Member newFriend;

    public Friend() {
    }

    @Builder
    public Friend(Member me, Member friend) {
        this.me = me;
        this.newFriend = friend;
    }

    public static Friend createFriend(Member me, Member friend) {

        return Friend.builder()
                .me(me)
                .friend(friend)
                .build();

    }
}















