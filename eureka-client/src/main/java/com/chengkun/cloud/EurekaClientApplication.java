package com.chengkun.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApplication.class, args);
    }

    @Value("${server.port}")
    String port;

    @RequestMapping("/hi")
    public String home(@RequestParam(value = "name") String name) {
        return "hi " + name + " ,i am from port:" + port;
    }

    @RequestMapping("/asyncHi")
    public String asyncHi(@RequestParam(value = "name") String name) {

        Date sendDate = new Date();
        Timer dTimer = new Timer();
        dTimer.schedule(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                i++;
                System.out.println("执行异步方法" + i);
                if (i == 10) { //执行10次结束
                    dTimer.cancel();
                }
            }
        }, sendDate, 30 * 1000);//设置30秒执行一次

        return "asyncHi " + name + " ,i am from port:" + port;
    }
}
