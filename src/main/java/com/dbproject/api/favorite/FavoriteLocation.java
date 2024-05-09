package com.dbproject.api.favorite;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "favorite_location")
@Getter
@Setter
public class FavoriteLocation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_location_id")
    private Long id;

    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;


    public FavoriteLocation() {
    }

    @Builder
    public FavoriteLocation(Member member, Location location, String memo) {
        this.member = member;
        this.location = location;
        this.memo = memo;
    }

    public static FavoriteLocation of(Member member, Location location, String memo) {

        return FavoriteLocation.builder()
                .location(location)
                .member(member)
                .memo(memo)
                .build();
    }

    public void updateMemo(String memo) {
        this.memo = memo;
    }
}
