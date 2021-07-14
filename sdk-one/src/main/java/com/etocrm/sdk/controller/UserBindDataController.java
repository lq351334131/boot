package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.data.*;
import com.etocrm.sdk.service.DataService;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
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
@Api(tags = "数据总览")
@ApiSort(2)
public class UserBindDataController {

    @Autowired
    private DataService dataService;

    @ApiOperation(value = "数据总览-列表", notes = "")
    @RequestMapping(value = "/getUserBindDataList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<UserDataRepVO> getUserBindDataList(@RequestBody UserDataVO userDataVO) {
        return  dataService.getUserBindDataList(userDataVO);
    }

    @ApiOperation(value = "数据总览-头部", notes = "")
    @RequestMapping(value = "/getUserBindDataTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<TotalVO>> getUserBindDataTotal(@RequestBody UserDataTypeIdVO userDataTypeIdVO) {
        return  dataService.getUserBindDataTotal(userDataTypeIdVO);
    }

    @ApiOperation(value = "数据总览-用户昨日访问数据统计", notes = "")
    @RequestMapping(value = "/getUserVisitData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<YestDayRepVO>> getUserVisitData() {
        return  dataService.getUserVisitData();
    }

}
