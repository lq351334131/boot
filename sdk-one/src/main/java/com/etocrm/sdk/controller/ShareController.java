package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.databroad.DataBroadVO;
import com.etocrm.sdk.entity.share.*;
import com.etocrm.sdk.entity.user.UserDatailVO;
import com.etocrm.sdk.service.ShareService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/userShare")
@Api(tags = "分享")
@ApiSort(7)
public class ShareController {

    @Autowired
    private ShareService shareService;


    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "分享趋势-汇总", notes = "")
    @RequestMapping(value = "/getUserShare", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<ShareUserVO> getUserShare(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getUserShare(dataBroadVO);
    }

   /* @ApiOperation(value = "分享概览-用户裂变图", notes = "")
    @RequestMapping(value = "/GetShareFission", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result getShareFission(@RequestBody DataBroadVO dataBroadVO) {
        return  null;
    }*/

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分享概览-重度用户To-不调用", notes = "")
    @RequestMapping(value = "/getShareNumList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<ShareNumVO>> getShareNumList(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getShareNumList(dataBroadVO);
    }

    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分享概览-影响力用户Top10-不调用", notes = "")
    @RequestMapping(value = "/getShareTurnbackList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<ShareNumVO>> getShareTurnbackList(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getShareTurnbackList(dataBroadVO);
    }


    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "分享概览-性别分布-不调用", notes = "")
    @RequestMapping(value = "/getShareGenderList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<ShareGenderVO>> getShareGenderList(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getShareGenderList(dataBroadVO);
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "分享概览-贡献用户Top10-不调用", notes = "")
    @RequestMapping(value = "/getShareUserPlusNumList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<ShareNumVO>> getShareUserPlusNumList(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getShareUserPlusNumList(dataBroadVO);
    }

    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "分享概览-城市", notes = "")
    @RequestMapping(value = "/getAreaShareLeaderboard", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<ShareCityReqVO> getAreaShareLeaderboard(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getAreaShareLeaderboard(dataBroadVO);
    }

    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "分享概览-重度用户Top10", notes = "")
    @RequestMapping(value = "/getSharePageTop10", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<SharePageTop10VO>> getSharePageTop10(@RequestBody DataBroadVO dataBroadVO) {
        return shareService.getSharePageTop10(dataBroadVO);
    }

    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "页面分享分页", notes = "")
    @RequestMapping(value = "/getPageShareList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<SharePageRepVO>> getPageShare(@RequestBody SharePage sharePage) {
        return shareService.getPageShare(sharePage);
    }

    @ApiOperationSupport(order = 12)
    @RequestMapping(value = "/downLoadPageShareList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "页面分享分页下载", notes = "")
    public void downLoadPageShareList(HttpServletResponse response, @RequestBody PageShareListVO pageShareListVO) {
        List<SharePageRepVO> sharePageRepVO = shareService.downLoadPageShareList(pageShareListVO);
        ExcelUtils.writeExcel(response, sharePageRepVO, "页面分享分页下载", SharePageRepVO.class);
    }

    @ApiOperationSupport(order = 13)
    @RequestMapping(value = "/downLoadAreaShareLeaderboard", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "城市下载", notes = "")
    public void downLoadAreaShareLeaderboard(HttpServletResponse response, @RequestBody DataBroadVO dataBroadVO) {
        List<ShareCityVO> shareCityVOS = shareService.downLoadAreaShareLeaderboard(dataBroadVO);
        ExcelUtils.writeExcel(response, shareCityVOS, "城市下载", ShareCityVO.class);
    }

    @ApiOperationSupport(order = 14)
    @RequestMapping(value = "/downLoadAreaShareList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户城市下载-不调用", notes = "")
    public void downLoadAreaShareList(HttpServletResponse response, @RequestBody UserCitySeaVO userCitySeaVO) {
        List<UserCityRepVO> userCityRepVO = shareService.downLoadAreaShareList(userCitySeaVO);
        ExcelUtils.writeExcel(response, userCityRepVO, "用户城市下载", UserCityRepVO.class);
    }

    @ApiOperationSupport(order = 11)
    @RequestMapping(value = "/getAreaShareList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户城市-不调用", notes = "")
    public Result<PageUtils<UserCityRepVO>> getAreaShareList(HttpServletResponse response, @RequestBody UserCitySeaPageVO userCitySeaPageVO) {
        return shareService.getAreaShareList(userCitySeaPageVO);
    }

    @ApiOperationSupport(order = 18)
    @RequestMapping(value = "/getShareAreaUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "用户数据分页-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getShareAreaUserList(HttpServletResponse response, @RequestBody UserSearchVO userSearchVO) {
        return shareService.getShareAreaUserList(userSearchVO);
    }

    @ApiOperationSupport(order = 8)
    @RequestMapping(value = "/getUserShareList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "分享趋势-列表-折线图", notes = "")
    public Result<List<DateShareVO>> getUserShareList(HttpServletResponse response, @RequestBody DataBroadVO dataBroadVO) {
        return shareService.getUserShareList(dataBroadVO);
    }

    @ApiOperationSupport(order = 17)
    @RequestMapping(value = "/getUserSharePageList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "分享趋势-列表-折线图分页", notes = "")
    public Result<PageUtils<DateShareVO>> getSharePageUserList(HttpServletResponse response, @RequestBody DateSharePageVO dateSharePageVO) {
        return shareService.getSharePageUserList(dateSharePageVO);
    }

    @ApiOperationSupport(order = 16)
    @RequestMapping(value = "/getShareUserList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "分享趋势--用户数据-分页-不调用", notes = "")
    public Result<PageUtils<UserDatailVO>> getShareUserList(HttpServletResponse response, @RequestBody ShareUserListVO shareUserListVO) {
        return shareService.getShareUserList(shareUserListVO);
    }


}





