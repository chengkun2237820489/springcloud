package com.chengkun.cloud.controller;

import com.chengkun.cloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @GetMapping("/hello")
    public String helloController(@RequestParam("name") String name){
        return helloService.hiService(name);
    }
}
