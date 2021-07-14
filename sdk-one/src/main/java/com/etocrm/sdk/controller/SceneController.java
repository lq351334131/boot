package com.etocrm.sdk.controller;


import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.page.PageListResVO;
import com.etocrm.sdk.entity.scene.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.entity.user.UserLostReturnRepVO;
import com.etocrm.sdk.service.SceneService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/scene")
@Api(tags = "场景")
@ApiSort(6)
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "场景入口-列表", notes = "")
    @RequestMapping(value = "/dataGet", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<SceneRepVO> dataGet(@RequestBody DataBroadVO dataBroadVO) {
        return  sceneService.dataGet(dataBroadVO);
    }

    @RequestMapping(value = "/dataDetailDownLoad", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "场景详细下载", notes = "")
    @ApiOperationSupport(order = 3)
    public void getexcel(HttpServletResponse response, @RequestBody DataBroadVO dataBroadVO)  {
        List<SceneTotalDetailDomainRepVO> event = sceneService.getexcel(dataBroadVO);
        ExcelUtils.writeExcel(response,event,"场景详细下载", SceneTotalDetailDomainRepVO.class);
    }

    @ApiOperation(value = "场景值入口_场景值列表/单个场景值", notes = "")
    @RequestMapping(value = "/dataGetBySceneID", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperationSupport(order = 4)
    public Result<DataSceneIDVO> dataGetBySceneID(@RequestBody SceneIdVO sceneIdVO) {
        return  sceneService.dataGetBySceneID(sceneIdVO);
    }


    @RequestMapping(value = "/downloadUserGetBySceneID", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "场景详细id下载-不调用", notes = "")
    public void downloadUserGetBySceneID(HttpServletResponse response, @RequestBody SceneExcelVO sceneExcelVO) throws Exception {
        List<UserDatailVO> userDatailList = sceneService.getSceneIdexcel(sceneExcelVO);
        ExcelUtils.writeExcel(response,userDatailList,"场景详细用户信息下载", UserDatailVO.class);
    }

    @ApiOperation(value = "首页场景值明细列表", notes = "")
    @ApiOperationSupport(order = 5)
    @RequestMapping(value = "/dataDetailGet", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<SceneTotalDetailDomainRepVO>> dataDetailGet(@RequestBody SceneDataVO sceneDataVO) {
        return  sceneService.dataDetailGet(sceneDataVO);
    }

    @ApiOperation(value = "特定场景类型的用户清单（第二层级）-不调用", notes = "")
    @RequestMapping(value = "/userGet", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<PageListResVO>> getUserGet(@RequestBody SceneUserVO sceneUserVO) {
        return  sceneService.getUserGet(sceneUserVO);
    }

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "场景值入口-汇总", notes = "")
    @RequestMapping(value = "/getSceneTotal", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<SceneTotalVO> getSceneTotal(@RequestBody DataBroadVO dataBroadVO) {
        return   sceneService.getSceneTotal(dataBroadVO);
    }

    @ApiOperationSupport(order = 10)
    @ApiOperation(value = "场景值特定日期的用户清单(第三层级)-不调用", notes = "")
    @RequestMapping(value = "/userGetBySceneID", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<UserLostReturnRepVO>> getUserGetBySceneID(@RequestBody SceneDateVO sceneDateVO) {
        return  sceneService.dataGetBySceneID(sceneDateVO);
    }





}