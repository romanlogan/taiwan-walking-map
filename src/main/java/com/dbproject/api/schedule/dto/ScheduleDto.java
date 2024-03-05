package com.dbproject.api.schedule.dto;

import com.dbproject.api.hangOut.hangOut.HangOut;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDto {

    //나중에 장소 디테일 화면으로 연결
    private String locationId;

    private String locationName;

    private String friendEmail;

    private String friendName;

    private LocalDateTime departDateTime;

    public ScheduleDto() {
    }

    @Builder
    public ScheduleDto(String locationId, String locationName, String friendEmail, String friendName, LocalDateTime departDateTime) {
        this.locationId = locationId;
        this.locationName = locationName;
        this.friendEmail = friendEmail;
        this.friendName = friendName;
        this.departDateTime = departDateTime;
    }

    public static ScheduleDto createByHangOut(HangOut hangOut) {

        return ScheduleDto.builder()
                .locationId(hangOut.getLocation().getLocationId())
                .locationName(hangOut.getLocation().getName())
                .friendEmail(hangOut.getRequester().getEmail())
                .friendName(hangOut.getRequester().getName())
                .departDateTime(hangOut.getDepartDateTime())
                .build();
    }
}
