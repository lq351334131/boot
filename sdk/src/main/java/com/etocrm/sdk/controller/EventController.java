package com.etocrm.sdk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.eventVO.*;
import com.etocrm.sdk.service.EventService;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/download", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "事件下载", notes = "")
    public void getexcel(HttpServletResponse response,@RequestBody DownLoadExcel vo) throws Exception {
        List<EventListVO> event = eventService.getEvent(vo);
        ExcelUtils.writeExcel(response,event,"事件下载", EventListVO.class);
    }

    @ApiOperation(value = "事件下载url", notes = "")
    @RequestMapping(value = "/downLoadEventList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result downLoadEventList() {
        return eventService.downLoadEventList();
    }

    /**
     *
     * @Description 事件分析 /事件管理
     * @author xing.liu
     * @date 2020/9/16
     **/
    @RequestMapping(value = "/getEventList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getEventList(@RequestBody EventVO vo) {
        return eventService.getEventList(vo);
    }
    /**
     *@author: xing.liu
     * 获取事件列表,参数appKey
     *@ 2020/9/3
     */
    @ApiOperation(value = "获取事件列表", notes = "")
    @RequestMapping(value = "/getEventNameList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  getEventNameList(@RequestBody String body ){
        JSONObject json = JSON.parseObject(body);
        String appKey=json.getString("appKey");
        String tv=json.getString("tv");
        String tl=json.getString("tl");
        return  eventService.getEventNameList(appKey,tv,tl);
    }

    /**
     *@author: xing.liu
     *@ 2020/9/3
     * 事件分析-列表
     */
    @ApiOperation(value = "事件分析-列表", notes = "")
    @RequestMapping(value = "/getEventAnalysisList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  getEventAnalysisList(@RequestBody EventAnalysisVO vo ){
        return  eventService.getEventAnalysisList(vo);
    }

    /**
     * @Description: 事件添加
     * @author xing.liu
     */
    @ApiOperation(value = "事件添加", notes = "")
    @RequestMapping(value = "/addEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  addEvent(@RequestBody EventEditVO vo ){
        return  eventService.addEvent(vo);
    }


    /**
     * @Description: 事件编辑
     * @author xing.liu
     */
    @ApiOperation(value = "事件编辑", notes = "")
    @RequestMapping(value = "/editEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  editEvent(@RequestBody EventEditVO vo ){
        return  eventService.editEvent(vo);
    }

    /**
     * @Description: 获取单条事件信息
     * @author xing.liu
     */
    @ApiOperation(value = "根据事件id编辑", notes = "")
    @RequestMapping(value = "/getSingleEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  getSingleEvent(@RequestBody String body ){
        JSONObject json = JSON.parseObject(body);
        String appKey = json.getString("appKey");
        String  id= json.getString("id");
        return  eventService.getSingleEvent(appKey,id);
    }

   /**
    *
    * @Description：事件表与上报数据关联
    * @author xing.liu
    * @date 2020/9/16
    **/
    @RequestMapping(value = "/getEventAnalysisDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  getEventAnalysisDetails(@RequestBody EventAnalysisDeVo vo ){
        return  eventService.getEventAnalysisDetails(vo);
    }

    /**
     * @Description: 事件表与上报数据关联，未完成
     * 事件分析-查看详情-参数明细
     * @author xing.liu
     */
    @ApiOperation(value = "事件分析-查看详情-参数明细", notes = "")
    @RequestMapping(value = "/getEventParamDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  getEventParamDetails(@RequestBody EventParamDetails vo ){
        return  eventService.getEventParamDetails(vo);
    }

    /**
     * @Description:删除事件
     * @author xing.liu
     */
    @ApiOperation(value = "根据id删除事件", notes = "")
    @RequestMapping(value = "/delEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public   Result  delEvent(@RequestParam String id ){
        return  eventService.delEvent(id);
    }

}
