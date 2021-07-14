package com.etocrm.sdk.entity.databroad;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel(value = "通用")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataBroadRepVO {

    @ApiModelProperty(value = "总数据" )
    private TotalDataRepVO totalData;

    //页面访问
    @ApiModelProperty(value = "页面访问" )
    private List<PathDataRepVO>   pathDatas;

    @ApiModelProperty(value = "每天" )
    private List<PortalDetailDatasRepVO> portalDetailDatas;

    @ApiModelProperty(value = "区域" )
    private List<RegDataRepVO> regDatas;

    @ApiModelProperty(value = "场景" )
    private List<ScenDataRepVO> sceneDatas;

    @ApiModelProperty(value = "二维码" )
    private List<QRReqVO> qrDatas;
}
