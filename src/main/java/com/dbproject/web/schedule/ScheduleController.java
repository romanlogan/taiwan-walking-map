package com.dbproject.web.schedule;

import com.dbproject.api.schedule.service.ScheduleService;
import com.dbproject.api.schedule.service.ScheduleServiceImpl;
import com.dbproject.api.schedule.dto.GetScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public String getSchedule(Principal principal, Model model) {

        GetScheduleResponse response = scheduleService.getSchedule(principal.getName());

        System.out.println(response.getScheduleJsonArray());

        model.addAttribute("getScheduleResponse", response);

        return "schedule/schedule";
    }

}
