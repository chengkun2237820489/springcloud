package com.chengkun.cloud.service;
/**
 * sungrow all right reserved
 **/
import org.springframework.stereotype.Service;

/**
 * @Description 测试分布式事务业务层
 * @Author chengkun
 * @Date 2020/4/8 14:03
 **/
@Service
public interface TestService {
    public int insert();
}