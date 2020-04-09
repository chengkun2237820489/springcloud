package com.chengkun.cloud;

import com.chengkun.cloud.mapper.TestMapper;
import com.chengkun.cloud.pojo.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TcClient1Application.class)
public class TcClient1ApplicationTests {

    @Autowired
    TestMapper mapper;

    @Test
    public void contextLoads() {
        TestModel test = new TestModel();
        test.setName("name");
        test.setType(1);
        int insert = mapper.insert(test);
        System.out.println(insert);
    }

}
