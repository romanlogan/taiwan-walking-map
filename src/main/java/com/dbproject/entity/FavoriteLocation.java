package com.dbproject.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "favorite_location")
@Getter
@Setter
public class FavoriteLocation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_location_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String memo;

    public FavoriteLocation() {
    }

    @Builder
    public FavoriteLocation(Member member, Location location,String memo) {
        this.member = member;
        this.location = location;
        this.memo = memo;
    }

    public static FavoriteLocation createFavoriteLocation(Member member, Location location,String memo) {

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
