package com.etocrm.sdk.entity.Qr;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrCodeRepVO {

    @ExcelProperty(index=0,value = "二维码名称")
    @ApiModelProperty(value = "二维码名称")
    private String qrName;

    @ExcelProperty(index=1,value ="页面url" )
    @ApiModelProperty(value = "页面url")
    private String page;

    @ExcelProperty(index=2,value ="appSecret" )
    @ApiModelProperty(value = "appSecret")
    private String appSecret;

    @ExcelProperty(index=3,value = "所属组")
    @ApiModelProperty(value = "所属组")
    private String groupName;

    @ExcelProperty(index=4,value = "创建时间")
    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ExcelProperty(index=5,value = "样式")
    @ApiModelProperty(value = "样式")
    private String codeType;

    @ExcelProperty(index=6,value ="尺寸" )
    @ApiModelProperty(value = "尺寸")
    private String codeWidth;

}
