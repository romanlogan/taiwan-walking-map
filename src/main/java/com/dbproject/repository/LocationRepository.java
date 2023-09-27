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
    public List<Location> findByPostalAddressCity(@Param("PostalAddressCity") String postalAddressCity);
}
