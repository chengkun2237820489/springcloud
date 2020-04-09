package com.chengkun.cloud.mapper;

import com.chengkun.cloud.pojo.TestModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * sungrow all right reserved
 **/
@Mapper
public interface TestMapper {

    int insert(TestModel test);
}
