package com.dbproject.api.favorite;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteLocation, Long>,
                                            QuerydslPredicateExecutor<FavoriteLocation>,
                                             FavoriteRepositoryCustom {


    @Query("select fl from FavoriteLocation fl" +
            " where fl.location.locationId = :locationId")
    FavoriteLocation findByLocationId(@Param("locationId") String locationId);


    @Query("select fl from FavoriteLocation fl"+
            " where fl.location.locationId = :locationId"+
            " and fl.member.email = :email")
    FavoriteLocation duplicateFavoriteLocation(@Param("locationId") String locationId, @Param("email") String email);

    @Query("select fl from FavoriteLocation fl" +
            " where fl.member.email = :email" +
            " order by fl.location.name asc")
    List<FavoriteLocation> getFavoriteLocationList(String email);
}
