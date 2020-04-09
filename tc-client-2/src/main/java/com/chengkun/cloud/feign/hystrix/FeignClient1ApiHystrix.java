package com.chengkun.cloud.feign.hystrix;
/**
 * sungrow all right reserved
 **/

import com.chengkun.cloud.feign.FeignClient1Api;
import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import org.springframework.stereotype.Component;

/**
 * @Description feignClient熔断器
 * @Author chengkun
 * @Date 2020/4/8 15:38
 **/
@Component
public class FeignClient1ApiHystrix implements FeignClient1Api {
    @Override
    public String insertTcClient1() {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return "Feign调用tc-client-1失败";
    }
}