package com.etocrm.sdk.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcodegroup.DeleteQrGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.GetQrCodeGroupVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupAddVO;
import com.etocrm.sdk.entity.qrcodegroup.QrCodeGroupEditVO;
import com.etocrm.sdk.service.QrCodeGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description 二维码组
 * @Date 2020/10/13 10:59
 */
@RestController
@RequestMapping("/api/QrCodeGroup")
@Api(value = "二维码组API")
public class QrCodeGroupController {
    @Autowired
    private QrCodeGroupService qrCodeGroupService;

    /**
     * @Description: 添加二维码组
     */
    @ApiOperation(value = "添加二维码组", notes = "添加二维码组")
    @PostMapping(value = "/AddQrGroup")
    public Result addQrGroup(@RequestBody @Valid QrCodeGroupAddVO qrCodeGroupAddVO){
        return  this.qrCodeGroupService.addQrGroup(qrCodeGroupAddVO);
    }

    /**
     * @Description: 编辑二维码组
     */
    @ApiOperation(value = "编辑二维码组", notes = "编辑二维码组")
    @PostMapping(value = "/EditQrGroup")
    public Result editQrGroup(@RequestBody @Valid QrCodeGroupEditVO qrCodeGroupEditVO){
        return  this.qrCodeGroupService.editQrGroup(qrCodeGroupEditVO);
    }

    /**
     * @Description: 删除二维码组
     *
     */
    @ApiOperation(value = "删除二维码组", notes = "删除二维码组")
    @GetMapping(value = "/DeleteQrGroup")
    public Result deleteQrGroup(@Valid DeleteQrGroupVO deleteQrGroupVO){
        return  this.qrCodeGroupService.deleteQrGroup(deleteQrGroupVO);
    }

    /**
     * @Description: 查询二维码组
     *
     */
    @ApiOperation(value = "查询二维码组", notes = "查询二维码组")
    @GetMapping(value = "/GetQrCodeGroup")
    public Result getQrCodeGroup(@Valid GetQrCodeGroupVO getQrCodeGroupVO){
        return  this.qrCodeGroupService.getQrCodeGroup(getQrCodeGroupVO);
    }

}
