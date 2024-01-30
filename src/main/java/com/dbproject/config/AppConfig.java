package com.dbproject.config;

import com.dbproject.api.mbtTest.service.MBTServiceImpl;
import com.dbproject.web.mbtTest.MBTController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//DI 컨테이너
@Configuration
public class AppConfig {

    @Bean
    public MBTController mbtController() {

        return new MBTController(new MBTServiceImpl());
    }
}
