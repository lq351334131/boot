package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.service.DataBroadService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data")
public class DataBroadController {
    @Autowired
    private DataBroadService dataBroadService;

    @ApiOperation(value = "数据看板", notes = "")
    @RequestMapping(value = "/portalData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result portalData(@RequestBody DataBroadVO vo) {
        return  dataBroadService.portalData(vo);
    }


}
