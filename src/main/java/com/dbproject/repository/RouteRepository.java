package com.dbproject.repository;

import com.dbproject.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {



    List<Route> findByPostalAddressCity(String PostalAddressCity);

    Route findRouteById(Integer Id);
}
