package com.etocrm.sdk.controller;


import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.scene.SceneTotalDetailDomainRepVO;
import com.etocrm.sdk.entity.scene.SceneVO;
import com.etocrm.sdk.service.SceneService;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/scene")
public class SceneController {

    @Autowired
    private SceneService sceneService;

    @ApiOperation(value = "场景入口-列表", notes = "")
    @RequestMapping(value = "/dataGet", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result dataGet(@RequestBody SceneVO vo) {
        return  sceneService.dataGet(vo);
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "场景详细下载", notes = "")
    public void getexcel(HttpServletResponse response, @RequestBody SceneVO vo) throws Exception {
        List<SceneTotalDetailDomainRepVO> event = sceneService.getexcel(vo);
        ExcelUtils.writeExcel(response,event,"场景详细下载", SceneTotalDetailDomainRepVO.class);
    }

}
