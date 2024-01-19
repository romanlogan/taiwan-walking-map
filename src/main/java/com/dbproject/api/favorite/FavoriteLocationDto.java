package com.dbproject.api.favorite;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteLocationDto {

    private Long id;
    private String memo;

    private String locationId;
    private String name;
    private String address;
    private String region;
    private String picture1;


    @Builder
    private FavoriteLocationDto(Long id, String memo, String locationId, String name, String address, String region, String picture1) {
        this.id = id;
        this.memo = memo;
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.region = region;
        this.picture1 = picture1;
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
                .build();
    }
}
