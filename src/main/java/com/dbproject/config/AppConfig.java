package com.dbproject.config;

import com.dbproject.api.mbtTest.service.MBTServiceImpl;
import com.dbproject.web.mbtTest.MBTController;

public class AppConfig {

    public MBTController mbtController() {

        return new MBTController(new MBTServiceImpl());
    }
}
