package com.chengkun.cloud.api;

import com.chengkun.cloud.hystric.SchedualServiceHiHystric;
import com.netflix.hystrix.HystrixCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chengkun
 * @version v1.0
 * @create 2019/7/9 15:34
 **/
@FeignClient(value = "eureka-client", fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

    @RequestMapping(value = "/asyncHi", method = RequestMethod.GET)
    HystrixCommand<String> sayAsyncHiFromClientOne(@RequestParam(value = "name") String name);
}
