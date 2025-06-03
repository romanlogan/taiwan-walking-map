package com.dbproject.api.favorite.repository;


import com.dbproject.api.favorite.FavoriteLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteLocation, Long>,
                                            QuerydslPredicateExecutor<FavoriteLocation>,
                                             FavoriteRepositoryCustom {
    @Query("select fl from FavoriteLocation fl" +
            " where fl.location.locationId = :locationId" +
            " and fl.member.email = :email")
    FavoriteLocation findByLocationIdAndEmail(@Param("locationId") String locationId, @Param("email") String email);


    @Query("select fl from FavoriteLocation fl"+
            " where fl.location.locationId = :locationId"+
            " and fl.member.email = :email")
    FavoriteLocation findDuplicateFavoriteLocation(@Param("locationId") String locationId, @Param("email") String email);

    @Query("select fl from FavoriteLocation fl" +
            " where fl.member.email = :email" +
            " order by fl.location.name asc")
    List<FavoriteLocation> findFavoriteLocationListByEmail(String email);

    @Query("select fl from FavoriteLocation fl" +
            " where fl.member.email = :email")
    List<FavoriteLocation> findByMemberEmail(@Param("email") String email);

    @Modifying
    @Query("delete from FavoriteLocation fl" +
            " where fl.member.email = :email")
    void deleteByMemberEmail(@Param("email") String email);


    @Query("select count(fl) from FavoriteLocation fl" +
    " where fl.member.email = :email")
    long getTotalFavoriteCount(@Param("email") String email);

}
