package com.dbproject.api.schedule;

import com.dbproject.api.myPage.hangOut.hangOut.HangOut;
import com.dbproject.api.myPage.hangOut.hangOut.HangOutRepository;
import com.dbproject.api.schedule.dto.GetScheduleResponse;
import com.dbproject.api.schedule.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {

    private final HangOutRepository hangOutRepository;

    public GetScheduleResponse getSchedule(String email) {

        //1. respondent 가 나 인 약속 일정을 모두 찾아서
        List<HangOut> hangOutList = hangOutRepository.getHangOutListByRespondentEmail(email);
        List<ScheduleDto> scheduleDtoList = new ArrayList<>();
        JSONArray scheduleJsonArray = new JSONArray();


        //2. dto 로 변경하기 (requester 정보 포함)
        for (HangOut hangOut : hangOutList) {

            // shceduleDto
            ScheduleDto scheduleDto = ScheduleDto.createByHangOut(hangOut);
            scheduleDtoList.add(scheduleDto);

            //JSON
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", hangOut.getRequester().getName()+"와 함께 "+hangOut.getLocation().getName());
            jsonObject.put("start", hangOut.getDepartDateTime());
//            여기 url 에 상세 약속 페이지를 만들어서 연결하기
            jsonObject.put("url", "/location/" + hangOut.getLocation().getLocationId());
//            url: 'http://google.com/',
            scheduleJsonArray.add(jsonObject);
        }

        return new GetScheduleResponse(scheduleDtoList,scheduleJsonArray);
    }
}
