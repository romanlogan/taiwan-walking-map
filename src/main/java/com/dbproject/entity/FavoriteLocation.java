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


    public FavoriteLocation() {
    }

    @Builder
    public FavoriteLocation(Member member, Location location) {
        this.member = member;
        this.location = location;
    }

    public static FavoriteLocation createFavoriteLocation(Member member, Location location) {

        return FavoriteLocation.builder()
                .location(location)
                .member(member)
                .build();
    }
}
