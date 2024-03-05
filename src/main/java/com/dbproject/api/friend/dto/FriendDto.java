package com.dbproject.api.friend.dto;


import com.dbproject.api.friend.Friend;
import com.dbproject.api.member.memberImg.MemberImg;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
public class FriendDto {

    private Long id;

    private String friendEmail;

    private String friendName;

    private String friendAddress;

    private String friendImgUrl;

    @Builder
    public FriendDto(Long id, String friendEmail, String friendName, String friendAddress, String friendImgUrl) {
        this.id = id;
        this.friendEmail = friendEmail;
        this.friendName = friendName;
        this.friendAddress = friendAddress;
        this.friendImgUrl = friendImgUrl;
    }

    public static FriendDto from(Friend friend, Optional<MemberImg> friendImg) {

        String imgUrl;

        if (friendImg.isPresent()) {
            imgUrl = friendImg.get().getImgUrl();
        }else{
            imgUrl = "";
        }

        return FriendDto.builder()
                .id(friend.getId())
                .friendEmail(friend.getNewFriend().getEmail())
                .friendName(friend.getNewFriend().getName())
                .friendAddress(friend.getNewFriend().getAddress())
                .friendImgUrl(imgUrl)
                .build();
    }
}
