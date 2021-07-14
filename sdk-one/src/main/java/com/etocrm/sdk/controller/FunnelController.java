package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.funnel.FunnelAddVO;
import com.etocrm.sdk.entity.funnel.FunnelEditVO;
import com.etocrm.sdk.entity.funnel.FunnelOneVO;
import com.etocrm.sdk.entity.funnel.FunnelVO;
import com.etocrm.sdk.service.FunnelSerive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result addFunnel(@RequestBody FunnelAddVO funnelAddVO) {
        return funnelSerive.addFunnel(funnelAddVO);
    }

    @RequestMapping(value = "/editFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result editFunnel(@RequestBody FunnelEditVO funnelAddVO) {
        return funnelSerive.editFunnel(funnelAddVO);
    }
    @RequestMapping(value = "/delFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result delFunnel(@RequestBody String id) {
        return funnelSerive.delFunnel(id);
    }

    @RequestMapping(value = "/getSingleFunnel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getSingleFunnel(@RequestBody FunnelOneVO vo) {
        return funnelSerive.getSingleFunnel(vo);
    }

    //页面漏斗转化率

    @PostMapping(value = "getPageRate")
    public Result get(){
        return funnelSerive.getPageRate();

    }








}
