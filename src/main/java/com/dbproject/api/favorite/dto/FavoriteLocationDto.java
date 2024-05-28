package com.dbproject.api.favorite.dto;


import com.dbproject.api.favorite.FavoriteLocation;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteLocationDto {

    private Long favoriteLocationId;
    private String memo;

    private String locationId;
    private String name;
    private String address;
    private String region;
    private String picture1;
    private String longitude;
    private String latitude;

    @QueryProjection
    @Builder
    public FavoriteLocationDto(Long id, String memo, String locationId, String name, String address, String region, String picture1, String longitude, String latitude) {
        this.favoriteLocationId = id;
        this.memo = memo;
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.region = region;
        this.picture1 = picture1;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static FavoriteLocationDto of(FavoriteLocation favoriteLocation) {

        return FavoriteLocationDto.builder()
                .id(favoriteLocation.getId())
                .memo(favoriteLocation.getMemo())
                .locationId(favoriteLocation.getLocation().getLocationId())
                .name(favoriteLocation.getLocation().getName())
                .address(favoriteLocation.getLocation().getAddress())
                .region(favoriteLocation.getLocation().getRegion())
                .picture1(favoriteLocation.getLocation().getLocationPicture().getPicture1())
                .longitude(favoriteLocation.getLocation().getLongitude())
                .latitude(favoriteLocation.getLocation().getLatitude())
                .build();
    }

}
