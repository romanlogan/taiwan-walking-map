package com.dbproject.repository;


import com.dbproject.entity.FavoriteLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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


}
