package com.dbproject.web.mbtTest;

import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.service.MBTService;
import com.dbproject.api.mbtTest.service.MBTServiceImpl;
import com.dbproject.api.mbtTest.service.MBTServiceJPAImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mbtTest")
public class MBTController {

    private final MBTService mbtService;

    //jpa 를 사용해야할때는 qualifier 를 jpa 로 변경
    public MBTController(@Qualifier("MBTServiceJPAImpl") MBTService mbtService) {
        this.mbtService = mbtService;
    }

    @GetMapping("/getCity")
    public String getCity(Model model) {

        MBTCityDto city = mbtService.getCity();

        model.addAttribute("city", city);


        return "/mbtTest/mbtTest";

    }
}
