package com.chengkun.cloud.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * @author:Administrator
 * @date:2019/9/29
 * @description:
 */
@Service
@RefreshScope
public class ConfigService {

    @Value("${foo}")
    public String foo;

    public String getFoo(){
        return foo;
    }
}