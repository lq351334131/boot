package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.eventVO.DownLoadExcel;
import com.etocrm.sdk.entity.eventVO.EventListVO;
import com.etocrm.sdk.entity.funnel.*;
import com.etocrm.sdk.service.FunnelSerive;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @Description 漏斗分析
 * @author xing.liu
 * @date 2020/10/12
 **/
@RestController
@RequestMapping("/api/funnel")
public class FunnelController {

    @Autowired
    private FunnelSerive funnelSerive;

    @RequestMapping(value = "/getFunnelAnalysisList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getFunnelAnalysisList(@RequestBody FunnelVO vo) {
        return funnelSerive.getFunnelAnalysisList(vo);
    }

    @RequestMapping(value = "/addFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result addFunnel(@RequestBody FunnelAddVO vo) {
        return funnelSerive.addFunnel(vo);
    }

    @RequestMapping(value = "/delFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result delFunnel(@RequestParam String id) {
        return funnelSerive.delFunnel(id);
    }

    @RequestMapping(value = "/editFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result editFunnel(@RequestBody FunnelEditVO vo) {
        return funnelSerive.editFunnel(vo);
    }

    @RequestMapping(value = "/getSingleFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getSingleFunnel(@RequestBody FunnelOneVO vo) {
        return funnelSerive.getSingleFunnel(vo);
    }

    @RequestMapping(value = "/downloadFunnelAnalysisList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public void downloadFunnelAnalysisList(HttpServletResponse response, @RequestBody DownLoadFunnelExcel vo) throws Exception {
        List<FunnelRepExcelVO> event = funnelSerive.downloadFunnelAnalysisList(vo);
        ExcelUtils.writeExcel(response,event,"漏斗分析-漏斗列表-下载", FunnelRepExcelVO.class);
    }





}
