package com.dbproject.api.myPage.hangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.friend.Friend;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
//1 대 1 로 놀러가는 계획
public class HangOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hang_out_id")
    private Long id;

    private LocalDateTime departDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_location_id")
    private FavoriteLocation favoriteLocation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_email",
            referencedColumnName = "member_email")
    private Member me;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_email",
            referencedColumnName = "member_email")
    private Member newFriend;




}
