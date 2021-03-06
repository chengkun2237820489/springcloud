package com.chengkun.cloud.controller;

import com.chengkun.cloud.api.SchedualServiceHi;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author:Administrator
 * @date:2019/7/9
 * @description:
 */
@RestController
public class HiController {

    @Autowired
    SchedualServiceHi schedualServiceHi;

    @GetMapping("/hi")
    public String hiController(@RequestParam(value = "name") String name) {
        return schedualServiceHi.sayHiFromClientOne(name);
    }

    @GetMapping("/asyncHi")
    public String asyncHiController(@RequestParam(value = "name") String name) throws ExecutionException, InterruptedException {
        //同步调用eureka-client服务的/hi接口
        String s = schedualServiceHi.sayHiFromClientOne(name);
        System.out.println(s);
        //异步调用eureka-client服务的/asyncHi接口
        Future<String> queue = schedualServiceHi.sayAsyncHiFromClientOne(name).queue();
        String s1 = queue.get();
        System.out.println(s1);
        return s;
    }
}
