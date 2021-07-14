package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.UserDataVO;
import com.etocrm.sdk.entity.YestDayVO;
import com.etocrm.sdk.service.UserBindService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description 数据总览
 * @author xing.liu
 * @date 2020/9/22
 **/
@RestController
@RequestMapping("/api/userBindData")
public class UserBindDataController {

    @Autowired
    private UserBindService  userBindService;

    @ApiOperation(value = "数据总览-列表", notes = "")
    @RequestMapping(value = "/getUserBindDataList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getUserBindDataList(@RequestBody UserDataVO vo) {
        return  userBindService.getUserBindDataList(vo);
    }

    @ApiOperation(value = "数据总览-头部", notes = "")
    @RequestMapping(value = "/getUserBindDataTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getUserBindDataTotal(@RequestBody UserDataVO vo) {
        return  userBindService.getUserBindDataTotal(vo);
    }

    @ApiOperation(value = "数据总览-用户昨日访问数据统计", notes = "")
    @RequestMapping(value = "/getUserVisitData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getUserVisitData(@RequestBody List<YestDayVO> vo) {
        return  userBindService.getUserVisitData(vo);
    }



}
