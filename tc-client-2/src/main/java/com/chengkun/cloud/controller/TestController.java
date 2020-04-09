package com.chengkun.cloud.controller;
/**
 * sungrow all right reserved
 **/

import com.chengkun.cloud.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 测试分布式事务
 * @Author chengkun
 * @Date 2020/4/8 14:05
 **/
@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/insertTcClient2")
    public String insertTcClient2() {
        int insert = testService.insert();
        System.out.println("调用tc-client-2的insertTcClient2成功");
        return "调用tc-client-2成功,id = " + insert;
    }

    @GetMapping("/insertTcClient2ByTcClient1")
    public String insertTcClient1() {
        int insert = testService.insertTcClient2ByTcClient1();
        System.out.println("调用tc-client-2的insertTcClient2ByTcClient1成功");
        return "调用tc-client-2成功,id = " + insert;
    }
}