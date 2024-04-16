package com.dbproject.api.route;

import com.dbproject.api.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {



//    List<Route> findByPostalAddressCity(String PostalAddressCity);

//    Route findRouteById(Integer Id);
}
