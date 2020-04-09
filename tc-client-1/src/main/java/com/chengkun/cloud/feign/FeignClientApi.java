package com.chengkun.cloud.feign;

import com.chengkun.cloud.feign.hystrix.FeignClientApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * sungrow all right reserved
 **/
@FeignClient(value = "tc-client-2", fallback = FeignClientApiHystrix.class)
public interface FeignClientApi {

    @GetMapping("/insertTcClient2ByTcClient1")
    public String insertTcClient2ByTcClient1();
}
