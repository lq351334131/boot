package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.campaign.CampaignVO;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.online.AddCartVO;
import com.etocrm.sdk.entity.online.OnlineTotalData;
import com.etocrm.sdk.entity.online.SearchVO;
import com.etocrm.sdk.entity.online.VisitpathVO;
import com.etocrm.sdk.service.EventService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event")
@Slf4j
@Api(tags = "事件API")
public class EventController {

    @Autowired
    private EventService eventService;

//
//
//    @ApiOperation(value = "事件新增", notes = "")
//    @RequestMapping(value = "/AddEvent", method = RequestMethod.POST)
//    @ResponseBody
//    public Result addEvent(@RequestBody EventAddVO eventAddVO) {
//        return  eventService.addEvent(eventAddVO);
//    }
//
//    @ApiOperation(value = "事件删除", notes = "")
//    @RequestMapping(value = "/DelEvent", method = RequestMethod.POST)
//    @ResponseBody
//    public Result deleteEvent(@RequestBody String id) {
//        return  eventService.deleteEvent(id);
//    }
//
//
//    @ApiOperation(value = "事件编辑", notes = "")
//    @RequestMapping(value = "/EditEvent", method = RequestMethod.POST)
//    @ResponseBody
//    public Result editEvent(@RequestBody EventAddVO eventAddVO) {
//        return  eventService.editEvent(eventAddVO);
//    }
//
//    @RequestMapping(value = "/GetEventList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getEventList(@RequestBody EventVO eventVO) {
//        return eventService.getEventList(eventVO);
//    }
//
//    @RequestMapping(value = "/DownLoadEventList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    @ApiOperation(value = "事件管理-下载", notes = "")
//    public void downloadPageTemplate( HttpServletResponse response,@RequestBody DownLoadExcel downLoadExcel){
//        List<EventListVO> eventListVOS = eventService.downloadPageTemplate(downLoadExcel);
//        ExcelUtils.writeExcel(response,eventListVOS,"页面管理下载", EventListVO.class);
//    }
//
//
//    @RequestMapping(value = "/GetEventNameList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getEventNameList(@RequestBody DownLoadExcel downLoadExcel) {
//        return eventService.getEventNameList(downLoadExcel);
//    }
//
//    @RequestMapping(value = "/GetSingleEvent", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getSingleEvent(@RequestBody String id) {
//        return eventService.getSingleEvent(id);
//    }
//
//    /**
//     *
//     * @Description 事件分析-列表
//     * @author xing.liu
//     * @date 2020/11/24
//     **/
//    @RequestMapping(value = "/GetEventAnalysisList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getEventAnalysisList(@RequestBody EventAnalysisVO eventAnalysisVO) {
//        return eventService.getEventAnalysisList(eventAnalysisVO);
//    }
//
//    //事件分析-查看详情还没做完
//    @RequestMapping(value = "/GetEventAnalysisDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result GetEventAnalysisDetails(@RequestBody EventIDVO eventIDVO) {
//        return eventService.getEventAnalysisDetails(eventIDVO);
//    }
//
//    /**
//     *
//     * @Description
//     * @author xing.liu
//     * @date 2020/11/25
//     **/
//    @RequestMapping(value = "/GetEventParamList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getEventParamList(@RequestBody ParamIDVO paramIDVO) {
//        return eventService.getEventParamList(paramIDVO);
//    }
//
//    @RequestMapping(value = "/GetEventParamDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getEventParamDetails(@RequestBody ParamSearchVO paramSearchVO) {
//        //未完成
//        return eventService.getEventParamDetails(paramSearchVO);
//    }
//
//    @RequestMapping(value = "/GetUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    public Result getUserList(@RequestBody EventUserVO eventUserVO) {
//        return eventService.getUserList(eventUserVO);
//    }
//
//    @RequestMapping(value = "/DownLoadUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public void downLoadUserList( HttpServletResponse response,@RequestBody EventUserExcelVO eventUserExcelVO){
//        List<EventUserRepVO> eventListVOS = eventService.downLoadUserList(eventUserExcelVO);
//        ExcelUtils.writeExcel(response,eventListVOS,"用户下载", EventUserRepVO.class);
//    }
//
//    @RequestMapping(value = "/DownLoadEventAnalysisList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public void downLoadEventAnalysisList( HttpServletResponse response,@RequestBody EventAnalysisExcelVO eventUserExcelVO){
//        List<AnalysisListVO> eventListVOS = eventService.getEventAnalysisListExcel(eventUserExcelVO);
//        ExcelUtils.writeExcel(response,eventListVOS,"事件分析-列表-下载", AnalysisListVO.class);
//    }
//
//    @RequestMapping(value = "/DownLoadEventParamDetails", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public void downLoadEventParamDetails( HttpServletResponse response,@RequestBody EventAnalysisExcelVO eventUserExcelVO){
//        //未完成
//    }
//
//    /**
//     *
//     * @Description 事件分析-查看详情-用户数据-下载
//     * @author xing.liu
//     * @date 2020/11/27
//     **/
//    @RequestMapping(value = "/DownLoadDetailUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
//    public void downLoadDetailUserList( HttpServletResponse response,@RequestBody EventAnalysisExcelVO eventUserExcelVO){
//        //未完成
//    }

    /**
     * @Description 电商事件分析
     *  a） 电商PV　UV　（基于电商的店铺）
     * ｂ）电商商品访问ＰＶ　UV（基于电商的店铺）
     * ｃ）加购（基于电商的店铺）
     * ｄ）热搜　
     * @author xing.liu
     * @date 2021/5/25
     **/

    /*@RequestMapping(value = "/getSearch", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getSearch(@RequestBody DataBroadVO dataBroadVO) {
        return eventService.getSearch(dataBroadVO);
    }
  */


    @GetMapping(value = "/batchShopInsert")
    public  Result batchShopInsert(){
      return   eventService.batchShopInsert();
    }


    @GetMapping(value = "/batchAddCartInsert")
    public  Result batchAddCartInsert(){
        return   eventService.batchAddCartInsert();
    }


    @GetMapping(value = "/batchCustInsert")
    public  Result batchCustInsert(){
        return   eventService.batchCustInsert();
    }

    @GetMapping(value = "/batchSearchInsert")
    public  Result batchSearchInsert(){
        return   eventService.batchSearchInsert();
    }


    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "门店", notes = "")
    @PostMapping(value = "/getShop")
    public Result<OnlineTotalData> getShop(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getShop(dataBroadVO);
    }

    @PostMapping(value = "/getShopUrl")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "门店url", notes = "")
    public Result<List<VisitpathVO>> getShopUrl(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getShopUrl(dataBroadVO);
    }

    @PostMapping(value = "/getSearch")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "热搜总", notes = "")
    public Result<Integer> getSearch(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getSearch(dataBroadVO);
    }


    @ApiOperationSupport(order = 4)
    @PostMapping(value = "/getSearchTotal")
    @ApiOperation(value = "热搜明细", notes = "")
    public Result<List<SearchVO>> getSearchTotal(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getSearchTotal(dataBroadVO);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "加购总", notes = "")
    @PostMapping(value = "/getAddcart")
    public Result<OnlineTotalData> getAddcart(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getAddcart(dataBroadVO);
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "加购详情", notes = "")
    @PostMapping(value = "/getAddcartProduct")
    public Result<List<AddCartVO>> getAddcartProduct(@RequestBody DataBroadVO dataBroadVO) {
        return  eventService.getAddcartProduct(dataBroadVO);
    }

    /**
     *
     * @Description 基于Campion的接口需求---暂停
     * @author xing.liu
     * @date 2021/6/3
     **/
    @PostMapping(value = "/getCampion")
    public Result getCampion(@RequestBody CampaignVO campionVO){
        return eventService.getCampion(campionVO);
    }

    @PostMapping(value = "/getParamList")
    public Result getParamList(@RequestBody CampaignVO campionVO){
        return eventService.getParamList(campionVO);
    }

    @PostMapping(value = "/getParamDeatil")
    public Result getParamDeatil(@RequestBody CampaignVO campionVO){
        return eventService.getParamDeatil(campionVO);
    }

    /**
     *
     * @Description 前一小时数据
     * @author xing.liu
     * @date 2021/6/4
     **/
    @PostMapping
    public Result getParamDeatilTime(@RequestBody CampaignVO campionVO){
        return eventService.getParamDeatil(campionVO);
    }


}
