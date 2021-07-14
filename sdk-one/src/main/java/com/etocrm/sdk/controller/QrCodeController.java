package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.Qr.*;
import com.etocrm.sdk.service.QrCodeService;
import com.etocrm.sdk.util.ExcelUtils;
import com.etocrm.sdk.util.PageUtils;
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
@RequestMapping("/api/qrCode")
@Api(value = "二维码", tags = "二维码")
@ApiSort(9)
public class QrCodeController {


    @Autowired
    private QrCodeService qrCodeService;

    @ApiOperation(value = "二维码添加", notes = "")
    @RequestMapping(value = "/addQrCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> addQrCode(@RequestBody AddQrCodeVO addQrCodeVO) {
        return  qrCodeService.addQrCode(addQrCodeVO);
    }

    @ApiOperation(value = "二维码编辑", notes = "")
    @RequestMapping(value = "/editQrCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> editQrCode(@RequestBody EditQrCodeVO editQrCodeVO) {
        return  qrCodeService.editQrCode(editQrCodeVO);
    }

    @ApiOperation(value = "二维码删除", notes = "")
    @RequestMapping(value = "/deleteQrCode", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<String> deleteQrCode(@RequestBody DeleteQrCodeVO deleteQrCode) {
        return  qrCodeService.deleteQrCode(deleteQrCode);
    }


    @ApiOperation(value = "二维码列表", notes = "")
    @RequestMapping(value = "/getQrListPagging", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<PageUtils<QrCodeRepVO>> getQrCodeListPagging(@RequestBody QrCodePageVO queryCodePageVO) {
        return  qrCodeService.getQrCodeListPagging(queryCodePageVO);
    }

    @RequestMapping(value = "/downLoadQrListPagging", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ApiOperation(value = "二维码下载", notes = "")
    public void downLoadQrListPagging(HttpServletResponse response, @RequestBody QrCodeVO qrCodeVO) {
        List<QrCodeRepVO> list=qrCodeService.downLoadQrListPagging(qrCodeVO);
        ExcelUtils.writeExcel(response, list, "二维码下载", QrCodeRepVO.class);
    }

    @ApiOperation(value = "单个二维码信息查询", notes = "")
    @RequestMapping(value = "/getQrData", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public Result<List<QrCodeRepVOParVO>> getQrData(@RequestBody QrCodeParam qrCodeParam) {
        return  qrCodeService.getCodeParam(qrCodeParam);
    }








}
