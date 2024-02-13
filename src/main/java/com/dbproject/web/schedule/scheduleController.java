package com.dbproject.web.schedule;

import com.dbproject.api.schedule.ScheduleService;
import com.dbproject.api.schedule.dto.GetScheduleResponse;
import com.dbproject.api.schedule.dto.ScheduleDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class scheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/schedule")
    public String getSchedule(Principal principal, Model model) {

        GetScheduleResponse response = scheduleService.getSchedule(principal.getName());

        System.out.println(response.getScheduleJsonArray());

        model.addAttribute("getScheduleResponse", response);

        return "schedule/schedule";
    }

}
