package com.chengkun.cloud.hystric;

import com.chengkun.cloud.api.SchedualServiceHi;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.stereotype.Component;

/**
 * @author:Administrator
 * @date:2019/7/11
 * @description:
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {

    private static final HystrixCommand.Setter hystrixCommandGroupKey =
            HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("SchedualServiceHi"));

    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry " + name;
    }

    @Override
    public HystrixCommand<String> sayAsyncHiFromClientOne(String name) {
        //异步调用失败
        return new HystrixCommand<String>(hystrixCommandGroupKey) {
            @Override
            protected String run() throws Exception {
                // TODO Auto-generated method stub
                System.out.println("Feign 服务异步调用服务降级");
                return "sorry " + name;
            }
        };
    }
}
