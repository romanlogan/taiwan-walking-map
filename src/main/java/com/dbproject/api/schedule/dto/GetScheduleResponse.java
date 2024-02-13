package com.dbproject.api.schedule.dto;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetScheduleResponse {

    List<ScheduleDto> scheduleDtoList = new ArrayList<>();
    JSONArray scheduleJsonArray = new JSONArray();

    public GetScheduleResponse() {
    }

    public GetScheduleResponse(List<ScheduleDto> scheduleDtoList, JSONArray scheduleJsonArray) {
        this.scheduleDtoList = scheduleDtoList;
        this.scheduleJsonArray = scheduleJsonArray;
    }
}
