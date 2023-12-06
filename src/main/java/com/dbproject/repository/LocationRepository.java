package com.dbproject.repository;

import com.dbproject.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, String>, LocationRepositoryCustom {


    @Query("select l from Location l " +
            "where l.PostalAddressCity = :PostalAddressCity " +
            "order by l.attractionId desc")
    List<Location> findByPostalAddressCity(@Param("PostalAddressCity") String postalAddressCity);

    Location findByAttractionId(String attractionId);

    @Query("select l from Location l "+
            "where l.AttractionName = :AttractionName")
    Location findByAttractionName(@Param("AttractionName") String AttractionName);


    @Query("select l from Location l " +
            "where l.AttractionName like :searchQuery and l.PostalAddressCity = :searchCity")
    List<Location> findBySearchQuery(@Param("searchQuery") String searchQuery,
                                     @Param("searchCity") String searchCity);

}
