package com.etocrm.sdk.controller;



import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.VO.TestVO;
import com.etocrm.sdk.service.IndexService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;


/**
 * @Author qi.li
 * @create 2020/8/31 19:46
 */
@RestController
@RefreshScope
@RequestMapping("/sdk")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/test")
    public Result getTest2(@RequestBody TestVO test){

        return indexService.getIndex(test);
    }


}
