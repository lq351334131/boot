package com.etocrm.tag.service.impl;


import com.etocrm.sdk.base.ResponseCode;
import com.etocrm.sdk.base.Result;
import com.etocrm.tag.entity.VO.TestVO;
import com.etocrm.tag.serverapi.SdkService;
import com.etocrm.tag.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SdkService sdkService;


    @Override
    public Result selectListService(TestVO testVO) {
        List<TestVO> test=new ArrayList<>();
        TestVO testVO1=new TestVO();
        testVO1.setAge(22);
        testVO1.setAgent("fale");
        testVO1.setName("liqi");
        test.add(testVO1);
        TestVO testVO2=new TestVO();
        testVO2.setAge(32);
        testVO2.setAgent("male");
        testVO2.setName("liuxing");
        test.add(testVO2);
        TestVO testVO3=new TestVO();
        try {
            Result sdk= sdkService.getIndex(testVO1);
            if(null!=sdk){
                return Result.success(sdk);
            }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return Result.error(ResponseCode.CANT_FIND_USER);
    }
}
