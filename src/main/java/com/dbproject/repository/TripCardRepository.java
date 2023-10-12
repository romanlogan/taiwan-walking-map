package com.dbproject.repository;

import com.dbproject.entity.TripCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripCardRepository extends JpaRepository<TripCard, Integer> {
}
