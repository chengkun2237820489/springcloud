package com.chengkun.cloud.feign;

import com.chengkun.cloud.feign.hystrix.FeignClient1ApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * sungrow all right reserved
 **/
@FeignClient(value = "tc-client-1", fallback = FeignClient1ApiHystrix.class)
public interface FeignClient1Api {

    @GetMapping("/insertTcClient1")
    public String insertTcClient1();
}
