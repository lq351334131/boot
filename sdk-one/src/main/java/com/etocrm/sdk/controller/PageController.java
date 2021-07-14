package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.page.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.service.PageService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/page")
@Api(tags = "页面")
@ApiSort(5)
@Slf4j
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * @Description :下载模板
     * @author xing.liu
     * @date 2020/9/14
     **/
    @RequestMapping(value = "/downloadPageTemplate", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面分析-页面管理-下载模板", notes = "")
    @ApiOperationSupport(order = 1)
    public void downloadPageTemplate( HttpServletResponse response){
        ExcelUtils.writeExcel(response,new ArrayList<PageListDownloadVO>(),"页面管理下载", PageListDownloadVO.class);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "页面管理-导入excel", notes = "")
    @RequestMapping(value = "/importPage", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> importExcel(@RequestParam("file")MultipartFile file) {
        return  pageService.importExcel(file);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "页面管理", notes = "")
    @RequestMapping(value = "/getPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<PageListResVO>> getPageList(@RequestBody PageListVO vo) {
        return pageService.getPageList(vo);
    }

    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "页面管理根据id获取单条数据", notes = "")
    @RequestMapping(value = "/getSinglePage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageListResVO> GetSinglePage(@RequestBody String  id) {
        return pageService.getSinglePage(id);
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "页面管理-编辑")
    @RequestMapping(value = "/editPageInfo", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> EditPageInfo(@RequestBody PageListResVO pageListResVO) {
        return pageService.updatePage(pageListResVO);
    }

    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "页面参数管理", notes = "")
    @RequestMapping(value = "/getPageParameterList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result<PageUtils<PageListResVO>> getPageParameterList(@RequestBody PageChannelVO vo)  {
        return pageService.getPageParameterList(vo);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "页面参数新增", notes = "")
    @RequestMapping(value = "/addPageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Result<String> addPageParameter(@RequestBody PageChannelAddVO pageChannelAddVO)  {
        return pageService.addChannel(pageChannelAddVO);
    }
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "页面参数编辑", notes = "")
    @RequestMapping(value = "/editPageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> editPageParameter(@RequestBody PageChannelEditVO  pageChannelEditVO) {
        return pageService.editPageParameter(pageChannelEditVO);
    }

    @ApiOperationSupport(order = 9)
    @RequestMapping(value = "/getSinglePageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "获取单个页面参数", notes = "")
    public Result<PageChannelEditVO> getSingleChallerPageParameter(@RequestBody String  id) {
        return pageService.getSinglePageParameter(id);
    }

    @ApiOperationSupport(order = 10)
    @RequestMapping(value = "/deletePageParameter", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "删除页面参数", notes = "")
    public Result<String> deletePageParameter(@RequestBody String  id) {
        return pageService.deleteId(id);
    }

    @ApiOperationSupport(order = 11)
    @ApiOperation(value = "页面访页--受访页-汇总", notes = "")
    @RequestMapping(value = "/getVisitPage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<VisitTotalVO> getVisitPage(@RequestBody PageAccessVo pageAccessVo) {
        return pageService.getVisitPage(pageAccessVo);
    }

    @ApiOperationSupport(order = 12)
    @ApiOperation(value = "页面访页--受访页-list", notes = "")
    @RequestMapping(value = "/getVisitPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<VisitTotalPVO>> getVisitPageList(@RequestBody VisitPageVO visitPageVO) {
        return pageService.getVisitPageList(visitPageVO);
    }
    @ApiOperationSupport(order = 13)
    @ApiOperation(value = "页面访页--入口页-汇总", notes = "")
    @RequestMapping(value = "/getHomePage", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<HomeTotalVO> getHomePage(@RequestBody PageAccessVo vo) {
        return pageService.getHomePage(vo);
    }

    @ApiOperationSupport(order = 14)
    @ApiOperation(value = "页面访页--入口页-list", notes = "")
    @RequestMapping(value = "/getHomePageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<HomeVO>> getHomePageList(@RequestBody VisitPageVO visitPageVO) {
        return pageService.getHomePageList(visitPageVO);
    }

    @ApiOperationSupport(order = 15)
    @RequestMapping(value = "/downLoadHomePageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "入口页-下载", notes = "")
    public void downLoadHomePageList( HttpServletResponse response, @RequestBody VisitVO visitVO){
        List<HomePageExcel> event = pageService.downLoadHomePageList(visitVO);
        ExcelUtils.writeExcel(response,event,"入口页-下载", HomePageExcel.class);
    }

    @ApiOperationSupport(order = 16)
    @RequestMapping(value = "/downLoadVisPageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "受访页-下载", notes = "")
    public void downLoadVisPageList( HttpServletResponse response, @RequestBody VisitVO visitVO){
        List<VisitPageExcel> event = pageService.downLoadVisPageList(visitVO);
        ExcelUtils.writeExcel(response,event,"受访页-下载", VisitPageExcel.class);
    }

    @ApiOperationSupport(order = 17)
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面分析-用户数据-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getUserList(@RequestBody PageUserVO userVO) {
        return pageService.getUserList(userVO);

    }

    @ApiOperationSupport(order = 18)
    @RequestMapping(value = "/downLoadUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    @ApiOperation(value = "页面分析-用户数据-下载-不调用", notes = "")
    public void downLoadUserList( HttpServletResponse response, @RequestBody PageUserVO userVO){
        List<UserDatailVO> userDatailVOList = pageService.downLoadUserList(userVO);
        ExcelUtils.writeExcel(response,userDatailVOList,"用户数据下载", UserDatailVO.class);
    }

    @ApiOperationSupport(order = 19)
    @RequestMapping(value = "/getPageVisitHabitFrequency", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯", notes = "")
    public Result<List<PageDepthVO>> getPageVisitHabitFrequency(@RequestBody PageVO pageVO) {
        return pageService.getPageVisitHabitFrequency(pageVO);
    }

    @ApiOperationSupport(order = 20)
    @RequestMapping(value = "/downLoadPageVisitHabitFrequency", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用频率", notes = "")
    public void downLoadPageVisitHabitFrequency(HttpServletResponse response,@RequestBody PageVO pageVO) {
        List<PageDepthVO> list = pageService.downLoadPageVisitHabitFrequency(pageVO);
        ExcelUtils.writeExcel(response, list, "使用频率列表", PageDepthVO.class);
    }

    @ApiOperationSupport(order = 21)
    @RequestMapping(value = "/getPageVisitHabitDepth", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用深度", notes = "")
    public Result<List<PageDepthVO>> getPageVisitHabitDepth(@RequestBody PageVO pageVO) {
        return pageService.getPageVisitHabitDepth(pageVO);
    }

    @ApiOperationSupport(order = 22)
    @RequestMapping(value = "/downLoadPageVisitHabitDepth", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用深度下载", notes = "")
    public void downLoadPageVisitHabitDepth(HttpServletResponse response,@RequestBody PageVO pageVO) {
        List<PageDepthVO> list = pageService.downLoadPageVisitHabitDepth(pageVO);
        ExcelUtils.writeExcel(response, list, "使用频率列表", PageDepthVO.class);
    }

    @ApiOperationSupport(order = 23)
    @RequestMapping(value = "/getPageVisitHabitTime", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用时长", notes = "")
    public Result<List<PageDepthVO>> getPageVisitHabitTime(@RequestBody PageVO pageVO) {
        return pageService.getPageVisitHabitTime(pageVO);
    }

    @ApiOperationSupport(order = 24)
    @RequestMapping(value = "/downLoadPageVisitHabitTime", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "使用习惯-使用时长下载", notes = "")
    public void downLoadPageVisitHabitTime(HttpServletResponse response,@RequestBody PageVO pageVO) {
        List<PageDepthVO> list = pageService.downLoadPageVisitHabitTime(pageVO);
        ExcelUtils.writeExcel(response, list, "使用时长列表", PageDepthVO.class);
    }

    @ApiOperationSupport(order = 25)
    @RequestMapping(value = "/getPageNameList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面名称", notes = "")
    public Result<List<PageListResVO>> getPageNameList() {
        return pageService.getPageNameList();
    }

    /**
     *
     * @Description 页面参数数据-q数组
     * @author xing.liu
     * @date 2020/11/26
     **/
    @RequestMapping(value = "/getPageParameterDataList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面参数", notes = "")
    @ApiOperationSupport(order = 26)
    public Result<PageUtils<ParamDataRepVO>> getPageParameterDataList(@RequestBody ParamDataSearchVO paramDataSearchVO) {
        return pageService.getPageParameterDataList(paramDataSearchVO);
    }

    @RequestMapping(value = "/downloadPageParameterDataList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "面不同参数下载", notes = "")
    public void downloadPageParameterDataList(HttpServletResponse response,@RequestBody ParamDataSearchExcelVO paramDataSearchExcelVO) {
        List<ParamDataRepVO> list = pageService.downloadPageParameterDataList(paramDataSearchExcelVO);
        ExcelUtils.writeExcel(response, list, "页面不同参数下载", ParamDataRepVO.class);
    }

    @RequestMapping(value = "/getPageParameterUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "面不同参数用户-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getPageParameterUserList(@RequestBody PageParameterPageSearchVO pageParameterPageSearchVO) {
        return pageService.getPageParameterUserList(pageParameterPageSearchVO);
    }

    @RequestMapping(value = "/downloadPageParameterUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面不同参数用户下载-不调用", notes = "")
    public void downloadPageParameterUserList(HttpServletResponse response,@RequestBody PageParameterUserSeaVO pageParameterUserSeaVO) {
        List<UserDatailVO> list=pageService.downloadPageParameterUserList(pageParameterUserSeaVO);
        ExcelUtils.writeExcel(response, list, "页面不同参数用户下载", UserDatailVO.class);
    }

    /**
     *
     * @Description 和downloadPageParameterDataList接口一样
     * @author xing.liu
     * @date 2021/5/31 
     **/
    @RequestMapping(value = "/downloadPageParameterDetailsList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面访问-不同参数-下载明细数据", notes = "")
    public void downloadPageParameterDetailsList(HttpServletResponse response,@RequestBody ParamDataSearchExcelVO paramDataSearchExcelVO) {
        List<ParamDataRepVO> list = pageService.downloadPageParameterDataList(paramDataSearchExcelVO);
        ExcelUtils.writeExcel(response, list, "页面访问-不同参数-下载明细数据", ParamDataRepVO.class);

    }


    //页面路径接口未完成


}
