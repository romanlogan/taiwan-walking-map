package com.dbproject.api.route;

import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.route.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

//    @Query()
    Route findByInvitePlan(InvitePlan invitePlan);


//    List<Route> findByPostalAddressCity(String PostalAddressCity);

//    Route findRouteById(Integer Id);
}
