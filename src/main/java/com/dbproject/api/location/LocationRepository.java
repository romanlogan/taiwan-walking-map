package com.dbproject.api.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


//    @Query("select l from Location l " +
//            "where l.AttractionName like :searchQuery and l.PostalAddressCity = :searchCity")
//    List<Location> findBySearchQuery(@Param("searchQuery") String searchQuery,
//                                     @Param("searchCity") String searchCity);

}
