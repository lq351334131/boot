package com.etocrm.tag.controller;


import com.etocrm.sdk.base.Result;
import com.etocrm.tag.entity.VO.TestVO;
import com.etocrm.tag.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Author qi.li
 * @create 2020/8/31 19:46
 */
@RestController
//@RequestMapping ("/tag")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @PostMapping("/selectDbProcess")
    public Result selectList(@RequestBody TestVO testVO) {
        return indexService.selectListService(testVO);
    }





}
