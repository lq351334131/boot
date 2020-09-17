package com.etocrm.sdk.service.impl;


import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.VO.TestVO;
import com.etocrm.sdk.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qi.li
 * @create 2020/9/14 19:13
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {


    @Override
    public Result getIndex(TestVO testVO) {
        List<TestVO> test=new ArrayList<>();
        TestVO testVO1=new TestVO();
        testVO1.setAge(30);
        testVO1.setAgent("fale");
        testVO1.setName("zhangxinhua");
        test.add(testVO1);
        return Result.success(test);
    }


}
