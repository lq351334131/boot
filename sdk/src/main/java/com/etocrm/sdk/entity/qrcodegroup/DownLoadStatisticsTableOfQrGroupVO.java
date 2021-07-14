package com.etocrm.sdk.entity.qrcodegroup;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Date 2020/10/22 16:46
 */
@Data
@Api(value="二维码组下载VO")
public class DownLoadStatisticsTableOfQrGroupVO implements Serializable {

    @ApiModelProperty(value = "小程序id")
    @ExcelProperty(value = "小程序id",index = 0)
    private String appId;

    @ApiModelProperty(value = "二维码组名称")
    @ExcelProperty(value = "二维码组名称",index = 1)
    private String groupName;

    @ApiModelProperty(value = "二维码组描述")
    @ExcelProperty(value = "二维码组描述",index = 2)
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @ExcelProperty(value = "创建时间",index = 3)
    private String  createdTime;

    @ApiModelProperty(value = "扫码总次数")
    @ExcelProperty(value = "扫码总次数",index = 4)
    private long  visitNum;

    @ApiModelProperty(value = "扫码总人数")
    @ExcelProperty(value = "扫码总人数",index = 5)
    private long  userNum;

    @ApiModelProperty(value = "扫码新增人数")
    @ExcelProperty(value = "扫码新增人数",index = 6)
    private long  newVisitNum;

}
