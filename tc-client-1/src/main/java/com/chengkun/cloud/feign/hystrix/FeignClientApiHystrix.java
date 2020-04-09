package com.chengkun.cloud.feign.hystrix;
/**
 * sungrow all right reserved
 **/

import com.chengkun.cloud.feign.FeignClientApi;
import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import org.springframework.stereotype.Component;

/**
 * @Description feignClient熔断器
 * @Author chengkun
 * @Date 2020/4/8 15:38
 **/
@Component
public class FeignClientApiHystrix implements FeignClientApi {
    @Override
    public String insertTcClient2ByTcClient1() {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return "Feign调用tc-client-2失败";
    }
}