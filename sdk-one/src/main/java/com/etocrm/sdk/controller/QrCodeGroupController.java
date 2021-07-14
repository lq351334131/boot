package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.Qr.*;
import com.etocrm.sdk.service.QrCodeGroupService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/qrCodeGroup")
@Api(value = "二维码组", tags = "二维码组")
@ApiSort(8)
@Slf4j
public class QrCodeGroupController {

    @Autowired
    private QrCodeGroupService qrCodeGroupService;

    @ApiOperation(value = "二维码添加", notes = "")
    @RequestMapping(value = "/addQrGroup", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> addQrGroup(@RequestBody AddQrGroup addQrGroup) {
        return  qrCodeGroupService.addQrGroup(addQrGroup);
    }

    @ApiOperation(value = "二维码编辑", notes = "")
    @RequestMapping(value = "/editQrGroup", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> editQrGroup(@RequestBody EditQrGroup editQrGroup) {
        return  qrCodeGroupService.editQrGroup(editQrGroup);
    }

    @ApiOperation(value = "二维码删除", notes = "")
    @RequestMapping(value = "/deleteQrGroup", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> deleteQrGroup(@RequestBody DeleteQrGroup deleteQrGroup) {
        return  qrCodeGroupService.deleteQrGroup(deleteQrGroup);
    }


    @ApiOperation(value = "二维码列表", notes = "")
    @RequestMapping(value = "/getQrGroupListPagging", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<QrGroupRepVO>> getQrGroupListPagging(@RequestBody QueryGroupPageVO queryGroupPageVO) {
        return  qrCodeGroupService.getQrGroupListPagging(queryGroupPageVO);
    }

    @RequestMapping(value = "/downLoadQrGroupListPagging", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "二维码列表下载", notes = "")
    public void downLoadQrGroupListPagging(HttpServletResponse response, @RequestBody QrGroupList qrGroupList) {
        List<QrGroupRepVO> list=qrCodeGroupService.downLoadQrGroupListPagging(qrGroupList);
        ExcelUtils.writeExcel(response, list, "二维码列表下载", QrGroupRepVO.class);
    }

    @RequestMapping(value = "/getQrCodeGroup", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "二维码组/下拉清单", notes = "")
    public Result<List<QrGroupRepVO>> getQrCodeGroup( @RequestBody QrGroupList qrGroupList) {
        return qrCodeGroupService.getQrCodeGroup(qrGroupList);
    }


}
