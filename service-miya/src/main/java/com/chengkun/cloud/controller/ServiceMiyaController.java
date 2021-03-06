package com.chengkun.cloud.controller;

import com.chengkun.cloud.ServiceMiyaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author:Administrator
 * @date:2019/7/15
 * @description:
 */
@RestController
public class ServiceMiyaController {

    @Autowired
    RestTemplate restTemplate;

    private static final Logger LOG = Logger.getLogger(ServiceMiyaApplication.class.getName());

    @RequestMapping("/miya")
    public String info(){
        LOG.log(Level.INFO, "info is being called");
        return restTemplate.getForObject("http://localhost:8988/info",String.class);
    }

    @RequestMapping("/hi")
    public String home(){
        LOG.log(Level.INFO, "hi is being called");
        return "hi i'm miya!";
    }
}
