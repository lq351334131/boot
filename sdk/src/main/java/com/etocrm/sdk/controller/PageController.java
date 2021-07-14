package com.etocrm.sdk.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.VO.*;
import com.etocrm.sdk.service.PageService;
import com.etocrm.sdk.util.ExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/page")
@Api(value = "页面", tags = "页面")
@Slf4j
public class PageController {
    @Autowired
    private PageService pageService;

    @RequestMapping(value = "/getHomePage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getHomePage(@RequestBody PageAccessVo vo) {
        return pageService.getHomePage(vo);
    }

    @RequestMapping(value = "/getPageNameList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getPageNameList() {
        return pageService.getPageNameList();
    }

    @RequestMapping(value = "/getPageVisitHabitFrequency", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯", notes = "")
    public Result getPageVisitHabitFrequency(@RequestBody PageVO vo) {
        return pageService.getPageVisitHabitFrequency(vo);
    }

    /**
     * @Description:页面分析-页面管理
     * @author xing.liu
     * @date 2020/9/12
     **/
    @ApiOperation(value = "页面管理", notes = "")
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getPageList(@RequestBody PageListVO vo) {
        return pageService.getPageList(vo);
    }

    @RequestMapping(value = "/downloadPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面管理列表下载", notes = "")
    public void downloadPageList(HttpServletResponse response, @RequestBody PageDownloadVO vo)  {
        try {
            List<PageListDownloadVO> list = pageService.downloadPageList(vo);
            ExcelUtils.writeExcel(response, list, "页面管理列表", PageListDownloadVO.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());

        }
    }


    /**
     * @Description: 页面参数编辑取单条数据
     * @author xing.liu
     */
    @ApiOperation(value = "页面管理根据id获取单条数据", notes = "")
    @RequestMapping(value = "/getSinglePageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getSinglePageParameter(@RequestParam String  id) {
        return pageService.getSinglePageParameter(id);
    }

    /**
     * @Description: 页面参数编辑取单条数据编辑
     * @author xing.liu
     */
    @RequestMapping(value = "/editPageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result editPageParameter(@RequestBody PageEditVO vo) {
        return pageService.editPageParameter(vo);
    }

    /**
     * @Description :下载模板
     * @author xing.liu
     * @date 2020/9/14
     **/
    @RequestMapping(value = "/downloadPageTemplate", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面管理模板下载", notes = "")
    public void downloadPageTemplate( HttpServletResponse response) throws Exception {
        ExcelUtils.writeExcel(response,null,"页面管理下载", PageListDownloadVO.class);
    }

    /**
     * @Description :页面管理 /渠道管理
     * @author xing.liu
     * @date 2020/9/14
     **/
    @ApiOperation(value = "渠道管理-列表", notes = "")
    @RequestMapping(value = "/getPageParameterList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result getPageParameterList(@RequestBody PageChannelVO vo)  {
        return pageService.getPageParameterList(vo);
    }

    /**
     * @Description :页面管理 /渠道管理-编辑
     * @author xing.liu
     * @date 2020/9/14
     **/
    @ApiOperation(value = "渠道管理-编辑", notes = "")
    @RequestMapping(value = "/editChannel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result editChannel(@RequestBody PageChannelEditVO vo)  {
        return pageService.editChannel(vo);
    }

    /**
     * @Description :页面管理 /渠道管理-查询单个
     * @author xing.liu
     * @date 2020/9/14
     **/
    @RequestMapping(value = "/getChannelId", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result getChannelId(@RequestBody String id)  {
        return pageService.getChannelId(id);
    }

    @ApiOperation(value = "渠道管理-删除", notes = "")
    @RequestMapping(value = "/delChannel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result delChannel(@RequestBody String id)  {
        return pageService.delChannel(id);
    }

    /**
     * @Description :页面管理 /渠道管理-添加
     * @author xing.liu
     * @date 2020/9/14
     **/
    @ApiOperation(value = "渠道管理-添加", notes = "")
    @RequestMapping(value = "/addChannel", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result addChannel(@RequestBody PageChannelEditVO vo)  {
        return pageService.addChannel(vo);
    }

    /**
     * @Description :页面管理
     * @author xing.liu
     * @date 2020/9/14
     **/
    @ApiOperation(value = "页面管理-添加", notes = "")
    @RequestMapping(value = "/addPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result addPage(@RequestBody PageEditVO  vo)  {
        return pageService.addPage(vo);
    }

    @ApiOperation(value = "页面管理-导入excel", notes = "")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public Result importExcel(@RequestParam(value = "uploadFile", required = false) MultipartFile file) {

        return  pageService.importExcel(file);
    }

   /**
    * @Description 根据上报数据p与page表关联
    * 页面访页--受访页-列表
    * @author xing.liu
    * @date 2020/9/16
    **/
    @ApiOperation(value = "页面访页--受访页-列表", notes = "")
    @RequestMapping(value = "/getVisitPageList", method = RequestMethod.POST)
    @ResponseBody
    public Result getVisitPageList(@RequestBody VisitPageVO  vo ) {
        return  pageService.getVisitPageList(vo);
    }

    @ApiOperation(value = "页面访页--入口页--列表", notes = "")
    @RequestMapping(value = "/getHomePageList", method = RequestMethod.POST)
    @ResponseBody
    public Result getHomePageList(@RequestBody VisitPageVO  vo ) {
        return  pageService.getHomePageList(vo);
    }

    @ApiOperation(value = "页面访页--所属模块--列表", notes = "")
    @RequestMapping(value = "/getVisitModulePage", method = RequestMethod.POST)
    @ResponseBody
    public Result getVisitModulePage(@RequestBody ModelVO  vo ) {
        return  pageService.getVisitModulePage(vo);
    }

    @RequestMapping(value = "/getVisitPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getVisitPage(@RequestBody PageAccessVo vo) {
        return pageService.getVisitPage(vo);
    }

    @RequestMapping(value = "/downLoadVisitPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面访问列表下载", notes = "")
    public void downLoadVisitPageList(HttpServletResponse response, @RequestBody VisitVO vo)  {
        try {
            List<VisitPageResVO> list = pageService.downLoadVisitPageList(vo);
            ExcelUtils.writeExcel(response, list, "页面访问受访列表", VisitPageResVO.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
    }

    @RequestMapping(value = "/downLoadHomePageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面访问列表下载", notes = "")
    public void downLoadHomePageList(HttpServletResponse response, @RequestBody HomePageVO vo)  {
        try {
            List<HomePageExcel> list = pageService.downLoadHomePageList(vo);
            ExcelUtils.writeExcel(response, list, "页面访问人口列表", HomePageExcel.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
    }

    /**
     * 
     * @Description 使用习惯-访问深度
     * @author xing.liu
     * @date 2020/9/18
     **/
    @RequestMapping(value = "/getPageVisitHabitDepth", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯", notes = "")
    public Result getPageVisitHabitDepth(@RequestBody PageVO vo) {
        return pageService.getPageVisitHabitDepth(vo);
    }

    @RequestMapping(value = "/downLoadPageVisitHabitDepth", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "使用习惯-深度页面下载", notes = "")
    public void downLoadPageVisitHabitDepth(HttpServletResponse response, @RequestBody PageVO vo)  {
        try {
            List<PageDepthVO> list = pageService.downLoadPageVisitHabitDepth(vo);
            ExcelUtils.writeExcel(response, list, "深度页面列表", PageDepthVO.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
    }


    /**
     *
     * @Description 使用习惯-使用时长
     * @author xing.liu
     * @date 2020/9/18
     **/
    @RequestMapping(value = "/getPageVisitHabitTime", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用时长", notes = "")
    public Result getPageVisitHabitTime(@RequestBody PageVO vo) {
        return pageService.getPageVisitHabitTime(vo);
    }

    /**
     *
     * @Description 使用习惯-使用时长
     * @author xing.liu
     * @date 2020/9/18
     **/
    @RequestMapping(value = "/downLoadPageVisitHabitTime", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用时长", notes = "")
    public void downLoadPageVisitHabitTime(HttpServletResponse response,@RequestBody PageVO vo) {
        try {
            List<PageDepthVO> list = pageService.downLoadPageVisitHabitTime(vo);
            ExcelUtils.writeExcel(response, list, "深度页面列表", PageDepthVO.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
    }

    @RequestMapping(value = "/downLoadPageVisitHabitFrequency", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用频率", notes = "")
    public void downLoadPageVisitHabitFrequency(HttpServletResponse response,@RequestBody PageVO vo) {
        try {
            List<PageDepthVO> list = pageService.downLoadPageVisitHabitFrequency(vo);
            ExcelUtils.writeExcel(response, list, "使用频率列表", PageDepthVO.class);
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
    }

    @RequestMapping(value = "/getPagesPathFirst", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面路径--获取页面路径根节点列表", notes = "")
    public Result getPagesPathFirst(@RequestBody PageVO vo) {
        return pageService.getPagesPathFirst(vo);
    }

    /**
     * @Description:没有完成-需求
     * @author xing.liu
     * @date 2020/10/13
     **/
    @RequestMapping(value = "/getPagePathNode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面路径--获取页面路径", notes = "")
    public Result getPagePathNode(@RequestBody PageNodeVO vo) {
        return pageService.getPagePathNode(vo);
    }









}
