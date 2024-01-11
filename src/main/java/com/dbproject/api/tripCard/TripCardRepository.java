package com.dbproject.api.tripCard;

import com.dbproject.api.tripCard.TripCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripCardRepository extends JpaRepository<TripCard, Integer> {


    @Query("select t from TripCard t "+
            "where t.address like %:address%")
    List<TripCard> findTripCardByCity(@Param("address") String address);

}
