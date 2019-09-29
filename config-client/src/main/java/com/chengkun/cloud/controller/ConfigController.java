package com.chengkun.cloud.controller;

import com.chengkun.cloud.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Administrator
 * @date:2019/9/29
 * @description:
 */
@RestController
public class ConfigController {

    @Autowired
    ConfigService configService;

    @RequestMapping("/hi")
    public String hi(){
        return configService.getFoo();
    }
}