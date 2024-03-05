package com.dbproject.api.schedule.service;

import com.dbproject.api.schedule.dto.GetScheduleResponse;

public interface ScheduleService {

    GetScheduleResponse getSchedule(String email);
}
