package com.dbproject.api.routeLocation.repository;

import com.dbproject.api.plan.Plan;
import com.dbproject.api.routeLocation.RouteLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteLocationRepository extends JpaRepository<RouteLocation, Long> {
}
