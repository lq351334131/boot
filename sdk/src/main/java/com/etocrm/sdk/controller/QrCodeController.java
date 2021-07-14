package com.etocrm.sdk.controller;

import com.etocrm.sdk.base.Result;
import com.etocrm.sdk.entity.qrcode.DeleteQrCodeVO;
import com.etocrm.sdk.entity.qrcode.GetQrCodeListVO;
import com.etocrm.sdk.entity.qrcode.QrCodeAddVO;
import com.etocrm.sdk.entity.qrcode.QrCodeEditVO;
import com.etocrm.sdk.service.QrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Date 2020/10/13 17:41
 */
@RestController
@RequestMapping("/api/QrCode")
@Api(value = "二维码API")
public class QrCodeController {
    @Autowired
    private QrCodeService qrCodeService;

    /**
     * @Description: 添加二维码
     */
    @ApiOperation(value = "添加二维码", notes = "添加二维码")
    @PostMapping(value = "/AddQrCode")
    public Result addQrCode(@RequestBody @Valid QrCodeAddVO qrCodeAddVO){
        return  this.qrCodeService.addQrCode(qrCodeAddVO);
    }

    /**
     * @Description: 编辑二维码
     */
    @ApiOperation(value = "编辑二维码", notes = "编辑二维码")
    @PostMapping(value = "/EditQrCode")
    public Result editQrCode(@RequestBody @Valid QrCodeEditVO qrCodeEditVO){
        return  this.qrCodeService.editQrCode(qrCodeEditVO);
    }

    /**
     * @Description: 删除二维码
     *
     */
    @ApiOperation(value = "删除二维码", notes = "删除二维码")
    @GetMapping(value = "/DeleteQrCode")
    public Result deleteQrCode(@Valid DeleteQrCodeVO deleteQrCodeVO){
        return  this.qrCodeService.deleteQrCode(deleteQrCodeVO);
    }

    /**
     * @Description: 查询二维码
     */
    @ApiOperation(value = "查询二维码", notes = "查询二维码")
    @GetMapping(value = "/GetQrListPagging")
    public Result getQrListByPage(@Valid GetQrCodeListVO getQrCodeListVO){
        return  this.qrCodeService.getQrListByPage(getQrCodeListVO);
    }
}
