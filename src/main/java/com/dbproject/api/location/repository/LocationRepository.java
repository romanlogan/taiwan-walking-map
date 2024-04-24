package com.dbproject.api.location.repository;

import com.dbproject.api.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String>, LocationRepositoryCustom {


    @Query("select l from Location l " +
            "where l.region = :region " +
            "order by l.locationId desc")
    List<Location> findByRegion(@Param("region") String region);


    Location findByLocationId(String LocationId);

//    Optional<Location> findById(@Param("id") String id);

//    @Query("select l from Location l "+
//            "where l.AttractionName = :AttractionName")

    Location findByName(String name);


    @Query("select l from Location l" +
            " where l.name like CONCAT('%',:searchQuery,'%') and l.region = :searchCity" +
            " order by l.name asc")
    List<Location> findBySearchQueryAndSearchCity(@Param("searchQuery") String searchQuery,
                                                  @Param("searchCity") String searchCity,
                                                  Pageable pageable);

    @Query("select distinct l.town from Location l" +
            " where l.region = :region" +
            " order by l.town asc")
    List<String> findTownListByRegion(@Param("region") String region);

//    @Query("select l from Location l" +
//            " where l.region = :region" +
//            " and l.favoriteCount")
//    List<Location> findRecommendLocationListTop10(String region);

}
