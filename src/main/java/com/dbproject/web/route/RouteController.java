package com.dbproject.web.route;

import com.dbproject.api.route.RouteDtlDto;
import com.dbproject.api.route.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RouteController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;
    private final RouteService routeService;

    @GetMapping("/route/{routeNum}")
    public String getRouteDtl(@PathVariable("routeNum") String routeNum,
                              Model model) {

        Integer routeId = Integer.parseInt(routeNum);
        RouteDtlDto routeDtlDto = routeService.getRouteDtl(routeId);

        model.addAttribute("routeDtlDto", routeDtlDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        return "route/routeDtl";
    }
}









