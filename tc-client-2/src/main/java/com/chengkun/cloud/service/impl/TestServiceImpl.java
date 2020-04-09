package com.chengkun.cloud.service.impl;
/**
 * sungrow all right reserved
 **/

import com.chengkun.cloud.feign.FeignClient1Api;
import com.chengkun.cloud.mapper.TestMapper;
import com.chengkun.cloud.pojo.TestModel;
import com.chengkun.cloud.service.TestService;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 测试分布式事务
 * @Author chengkun
 * @Date 2020/4/8 14:05
 **/
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper mapper;

    @Autowired
    private FeignClient1Api feignClient1Api;

    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    @Transactional
    public int insert() {
        feignClient1Api.insertTcClient1();
        TestModel test = new TestModel();
        test.setName("name");
        test.setType(1);
        mapper.insert(test);
//        int a = 1/0;
        return test.getId();

    }

    @Override
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional
    public int insertTcClient2ByTcClient1() {
        TestModel test = new TestModel();
        test.setName("zhangsan");
        test.setType(2);
        mapper.insert(test);
//        int a = 1/0;
        return test.getId();
    }
}