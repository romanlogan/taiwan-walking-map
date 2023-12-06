package com.dbproject.controller;

import com.dbproject.dto.RouteDtlDto;
import com.dbproject.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/route/{routeNum}")
    public String getRouteDtl(@PathVariable("routeNum") String routeNum,
                              Model model) {

        Integer routeId = Integer.parseInt(routeNum);
        RouteDtlDto routeDtlDto = routeService.getRouteDtl(routeId);

        model.addAttribute("routeDtlDto", routeDtlDto);

        return "route/routeDtl";
    }
}









