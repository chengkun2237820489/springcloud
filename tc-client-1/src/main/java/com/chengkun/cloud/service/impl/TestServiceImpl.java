package com.chengkun.cloud.service.impl;
/**
 * sungrow all right reserved
 **/

import com.chengkun.cloud.feign.FeignClientApi;
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
    private FeignClientApi feignClientApi;

    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    @Transactional
    public int insert() {
        TestModel test = new TestModel();
        test.setName("name");
        test.setType(1);
        mapper.insert(test);
        feignClientApi.insertTcClient2ByTcClient1();
//        int a = 1/0;
        return test.getId();
    }
}